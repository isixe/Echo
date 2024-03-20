package dev.itea.echo.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import dev.itea.echo.entity.recommend.Word;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HanLP test
 *
 * @author: isixe
 * @create: 2024-03-19 20:58
 **/
@Slf4j
public class HanLpTests {

    private final String inputStr = "HanLP是面向生产环境的多语种自然语言处理工具包，基于PyTorch和TensorFlow 2.x双引擎，目标是普及落地最前沿的NLP技术。" +
            "HanLP具备功能完善、精度准确、性能高效、语料时新、架构清晰、可自定义的特点。" +
            "The multilingual NLP library for researchers and companies, built on PyTorch and TensorFlow 2.x, " +
            "for advancing state-of-the-art deep learning techniques in both academia and industry. " +
            "HanLP was designed from day one to be efficient, user-friendly and extendable.";

    /**
     * HanLP segment test
     */
    @Test
    void hanLpSegmentTest() {
        // StandardTokenizer
        List<Term> termList = StandardTokenizer.segment(inputStr);
        log.info(termList.toString());
        // HanLP
        List<Term> hanlpSegment = HanLP.segment(inputStr);
        log.info(hanlpSegment.toString());
        // NLPTokenizer
        List<Term> nlpSegment = NLPTokenizer.segment(inputStr);
        log.info(nlpSegment.toString());
    }

    /**
     * HanLP segment suggester test
     */
    @Test
    void hanLpSegmentSuggesterTest() {
        Suggester suggester = new Suggester();
        String[] inputList = inputStr.split("、");

        for (String s : inputList) {
            suggester.addSentence(s);
        }

        //word suggest
        List<String> wordSuggestList = suggester.suggest("HanLP", 1);
        log.info(wordSuggestList.toString());

        //phrase suggest
        List<String> phraseSuggestList = suggester.suggest("精准度高", 1);
        log.info(phraseSuggestList.toString());

        //pinyin suggest
        List<String> pinyinSuggestList = suggester.suggest("gaoxiao", 1);
        log.info(pinyinSuggestList.toString());
    }

    /**
     * HanLP segment extract test
     */
    @Test
    void hanLpSegmentExtractTest() {
        List<String> keywordList = HanLP.extractKeyword(inputStr, 10);
        log.info(keywordList.toString());
    }

    /**
     * HanLP segment summary test
     */
    @Test
    void hanLpSegmentSummaryTest() {
        List<String> sentenceList = HanLP.extractSummary(inputStr, 3);
        log.info(sentenceList.toString());
    }

    /**
     * HanLP segment phrase test
     */
    @Test
    void hanLpSegmentPhraseTest() {
        List<String> phraseList = HanLP.extractPhrase(inputStr, 10);
        log.info(phraseList.toString());
    }

    /**
     * Utils segment test
     */
    @Test
    void segmentUtilTest() throws IOException {
        List<Word> words = HanlpUtil.segment(inputStr);
        String wordStr = words.stream().map(Word::getName).collect(Collectors.joining(") ("));
        log.info(wordStr);
    }
}
