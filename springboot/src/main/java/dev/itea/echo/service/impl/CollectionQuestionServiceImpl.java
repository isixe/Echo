package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.CollectionArticle;
import dev.itea.echo.entity.CollectionQuestion;
import dev.itea.echo.mapper.CollectionQuestionMapper;
import dev.itea.echo.service.CollectionQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.CollectionQuestionVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 收藏表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class CollectionQuestionServiceImpl extends ServiceImpl<CollectionQuestionMapper, CollectionQuestion> implements CollectionQuestionService {

    @Resource
    CollectionQuestionMapper collectionQuestionMapper;

    @Override
    @CacheEvict(cacheNames = "collectionQuestion", key = "#id")
    public void delete(Integer id) {
        collectionQuestionMapper.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "collectionQuestion", key = "#id")
    public CollectionQuestion get(Integer id) {
        return collectionQuestionMapper.selectById(id);
    }

    @Override
    public IPage<CollectionQuestionVO> getPage(Pageable pageable, String keyword) {
        Page<CollectionQuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<CollectionQuestionVO> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("u.name", keyword)
                    .or()
                    .like("q.title", keyword);
        }
        return collectionQuestionMapper.getPage(page, wrapper);
    }

    @Override
    public IPage<CollectionQuestionVO> getPageByUserId(Pageable pageable, Integer userId) {
        Page<CollectionQuestionVO> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<CollectionQuestionVO> wrapper = new QueryWrapper<CollectionQuestionVO>().eq("cq.user_id", userId);
        return collectionQuestionMapper.getPage(page, wrapper);

    }

}
