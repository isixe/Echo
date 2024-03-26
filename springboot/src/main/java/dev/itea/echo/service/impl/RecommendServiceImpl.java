package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.UserPersonalize;
import dev.itea.echo.entity.recommend.Word;
import dev.itea.echo.mapper.RecommendMapper;
import dev.itea.echo.service.ArticleService;
import dev.itea.echo.service.QuestionService;
import dev.itea.echo.service.RecommendService;
import dev.itea.echo.service.UserPersonalizeService;
import dev.itea.echo.utils.ContentBaseUtils;
import dev.itea.echo.utils.HanlpUtil;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.QuestionVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 推荐 服务实现类
 *
 * @author: isixe
 * @create: 2024-03-19 15:47
 * @description: 用户行为数据来源：评论4, 收藏3, 点赞2, 历史1
 **/
@Slf4j
@Service
public class RecommendServiceImpl implements RecommendService {

    @Resource
    RecommendMapper recommendMapper;

    @Resource
    ArticleService articleService;

    @Resource
    QuestionService questionService;

    @Resource
    UserPersonalizeService userPersonalizeService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    @Scheduled(cron = "0 12 * * * *")
    public void seekArticleToRedis() {
        log.info("Scheduled start...");
        //get article data
        Pageable fullPage = PageRequest.of(1, 10000);
        List<ArticleVO> list = articleService.getPage(fullPage, null).getRecords();

        //get article map
        Map<String, String> articleMap = new HashMap<>();
        for (ArticleVO article : list) {
            articleMap.put(article.getId().toString(), new HtmlCleaner().clean(article.getContent()).getText().toString());
        }
        redisTemplate.opsForHash().putAll("articleMap", articleMap);

        //get content set
        Set<String> aContentSet = new HashSet<>(articleMap.values());

        //corpus
        Map<String, List<Double>> articleVector = getVector(articleMap, aContentSet);

        //save corpus vetor to redis
        String redisKey = "article_vector";
        redisTemplate.opsForHash().putAll(redisKey, articleVector);
        log.info("Scheduled end...");
    }

    @Override
    @Scheduled(cron = "0 14 * * * *")
    public void seekQuesionToRedis() {
        log.info("Scheduled start...");
        //get question data
        Pageable fullPage = PageRequest.of(1, 10000);
        List<QuestionVO> list = questionService.getPage(fullPage, null).getRecords();

        //get question map
        Map<String, String> questionMap = new HashMap<>();
        for (QuestionVO question : list) {
            questionMap.put(question.getId().toString(), new HtmlCleaner().clean(question.getContent()).getText().toString());
        }
        redisTemplate.opsForHash().putAll("questionMap", questionMap);

        //get content set
        Set<String> aContentSet = new HashSet<>(questionMap.values());

        //corpus
        Map<String, List<Double>> questionVector = getVector(questionMap, aContentSet);

        //save corpus vetor to redis
        String redisKey = "question_vector";
        redisTemplate.opsForHash().putAll(redisKey, questionVector);
        log.info("Scheduled end...");
    }

    @Override
    public IPage<ArticleVO> seekArticleByUserId(Pageable pageable, Integer userId) {
        //seekArticleToRedis();

        UserPersonalize userPersonalize = userPersonalizeService.get(userId);

        List<ArticleVO> list = new ArrayList<>();
        if (userPersonalize.isUseCommented()) {
            List<ArticleVO> commentArticleList = recommendMapper.getArticleWithCommented(userId);
            list.addAll(commentArticleList);
        }

        if (userPersonalize.isUseCollection()) {
            List<ArticleVO> collectArticleList = recommendMapper.getArticleFromCollection(userId);
            list.addAll(collectArticleList);
        }

        if (userPersonalize.isUseThumb()) {
            List<ArticleVO> thumbArticleList = recommendMapper.getArticleWithThumb(userId);
            list.addAll(thumbArticleList);
        }

        if (userPersonalize.isUseHistory()) {
            List<ArticleVO> historyArticleList = recommendMapper.getArticleFromHistory(userId);
            list.addAll(historyArticleList);
        }

        //get article map
        Map<String, String> rawArticleMap = new HashMap<>();
        for (ArticleVO article : list) {
            rawArticleMap.put(article.getId().toString(), new HtmlCleaner().clean(article.getContent()).getText().toString());
        }

        Map<Object, Object> corpusMap = redisTemplate.opsForHash().entries("articleMap");
        Map<String, String> corpusArticleMap = corpusMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> entry.getValue().toString()
                ));

        //get content set
        Set<String> aContentSet = new HashSet<>(corpusArticleMap.values());

        //raw
        Map<String, List<Double>> rawArticleVectorMap = getVector(rawArticleMap, aContentSet);

        //prepare for vector list
        String redisKey = "article_vector";
        Map<Object, Object> vectorMap = redisTemplate.opsForHash().entries(redisKey);
        Map<String, List<Double>> articleVectorMap = vectorMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> String.valueOf(entry.getKey()),
                        entry -> ((List<?>) entry.getValue()).stream()
                                .map(obj -> Double.parseDouble(String.valueOf(obj)))
                                .collect(Collectors.toList())
                ));

        Set<String> recommendArticleId = new HashSet<>();

        //computed cosine similarity and add to reconmmend set
        for (Map.Entry<String, List<Double>> rowEntry : rawArticleVectorMap.entrySet()) {
            String rowKey = rowEntry.getKey();
            List<Double> rowVector = rowEntry.getValue();

            for (Map.Entry<String, List<Double>> entry : rawArticleVectorMap.entrySet()) {
                String key = entry.getKey();
                List<Double> vector = entry.getValue();

                if (rowKey.equals(key)) {
                    continue;
                }

                Double similarity = ContentBaseUtils.cosineSimilarity(rowVector, vector);

                if (similarity > 0.8) {
                    recommendArticleId.add(key);
                }
            }
        }

        //log.info(recommendArticleId.toString());
        List<String> recommendIdList = recommendArticleId.stream().toList();
        return articleService.getPageByIdList(pageable, recommendIdList);
    }

    @Override
    public IPage<QuestionVO> seekQuestionByUserId(Pageable pageable, Integer userId) {
        //seekQuesionToRedis();

        UserPersonalize userPersonalize = userPersonalizeService.get(userId);

        List<QuestionVO> list = new ArrayList<>();

        if (userPersonalize.isUseCommented()) {
            List<QuestionVO> commentQuestionList = recommendMapper.getQuestionWithCommented(userId);
            list.addAll(commentQuestionList);
        }

        if (userPersonalize.isUseCollection()) {
            List<QuestionVO> collectQuestionList = recommendMapper.getQuestionFromCollection(userId);
            list.addAll(collectQuestionList);
        }

        if (userPersonalize.isUseThumb()) {
            List<QuestionVO> thumbQuestionList = recommendMapper.getQuestionWithThumb(userId);
            list.addAll(thumbQuestionList);
        }

        if (userPersonalize.isUseHistory()) {
            List<QuestionVO> historyQuestionList = recommendMapper.getQuestionFromHistory(userId);
            list.addAll(historyQuestionList);
        }

        //get question map
        Map<String, String> rawQuestionMap = new HashMap<>();
        for (QuestionVO question : list) {
            rawQuestionMap.put(question.getId().toString(), new HtmlCleaner().clean(question.getContent()).getText().toString());
        }

        Map<Object, Object> corpusMap = redisTemplate.opsForHash().entries("questionMap");
        Map<String, String> corpusQuestionMap = corpusMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> entry.getValue().toString()
                ));

        //get content set
        Set<String> aContentSet = new HashSet<>(corpusQuestionMap.values());
        //raw
        Map<String, List<Double>> rawArticleVectorMap = getVector(rawQuestionMap, aContentSet);

        //prepare for vector list
        String redisKey = "question_vector";
        Map<Object, Object> vectorMap = redisTemplate.opsForHash().entries(redisKey);
        Map<String, List<Double>> questionVectorMap = vectorMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> String.valueOf(entry.getKey()),
                        entry -> ((List<?>) entry.getValue()).stream()
                                .map(obj -> Double.parseDouble(String.valueOf(obj)))
                                .collect(Collectors.toList())
                ));

        Set<String> recommendQuestionId = new HashSet<>();

        //computed cosine similarity and add to reconmmend set
        for (Map.Entry<String, List<Double>> rowEntry : rawArticleVectorMap.entrySet()) {
            String rowKey = rowEntry.getKey();
            List<Double> rowVector = rowEntry.getValue();

            for (Map.Entry<String, List<Double>> entry : rawArticleVectorMap.entrySet()) {
                String key = entry.getKey();
                List<Double> vector = entry.getValue();

                if (rowKey.equals(key)) {
                    continue;
                }

                Double similarity = ContentBaseUtils.cosineSimilarity(rowVector, vector);

                if (similarity > 0.8) {
                    recommendQuestionId.add(key);
                }
            }
        }

        log.info(recommendQuestionId.toString());
        List<String> recommendIdList = recommendQuestionId.stream().toList();
        return questionService.getPageByIdList(pageable, recommendIdList);
    }

    public static Map<String, List<Double>> getVector(Map<String, String> articleMap, Set<String> aContentSet) {
        //get content set
        Map<String, List<Double>> articleVector = new HashMap<>();
        for (Map.Entry<String, String> entry : articleMap.entrySet()) {
            String aid = entry.getKey();
            String sentence = entry.getValue();

            List<Word> segmentList = HanlpUtil.segment(sentence);

            //compute word's TF, then sort by TF
            segmentList = segmentList.stream()
                    .peek(word -> word.setTf(ContentBaseUtils.computeTF(sentence, word.getName())))
                    .sorted((w1, w2) -> Double.compare(w2.getTf(), w1.getTf()))
                    .collect(Collectors.toList());

            //System.out.println(aid);

            //get top5 segment word
            List<Word> top5WordSegment = segmentList.subList(0, Math.min(5, segmentList.size()));
            //log.info("size:" + top5WordSegment.size());
            //compute top5 vector
            List<Double> vector = ContentBaseUtils.getDocVetor(aContentSet, top5WordSegment);
            if (vector.size() == 5) {
                articleVector.put(aid, vector);
            }
        }
        return articleVector;
    }
}
