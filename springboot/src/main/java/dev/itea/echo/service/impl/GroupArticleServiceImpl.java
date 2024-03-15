package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.GroupArticle;
import dev.itea.echo.mapper.GroupArticleMapper;
import dev.itea.echo.service.GroupArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 文章分组表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class GroupArticleServiceImpl extends ServiceImpl<GroupArticleMapper, GroupArticle> implements GroupArticleService {

    @Resource
    GroupArticleMapper groupArticleMapper;

    @Override
    @CacheEvict(cacheNames = "groupArticle", key = "#id")
    public void delete(Integer id) {
        groupArticleMapper.deleteById(id);
    }

    @Override
    @CachePut(cacheNames = "groupArticle", key = "#groupArticle.id")
    public GroupArticle update(GroupArticle groupArticle) {
        groupArticleMapper.updateById(groupArticle);
        return groupArticle;
    }

    @Override
    @Cacheable(cacheNames = "groupArticle", key = "#id")
    public GroupArticle get(Integer id) {
        return groupArticleMapper.selectById(id);
    }

    @Override
    public GroupArticle getByUserIdAndGroupName(Integer userId, String groupName) {
        return groupArticleMapper.selectOne(new QueryWrapper<GroupArticle>()
                .eq("user_id", userId)
                .eq("name", groupName));
    }

    @Override
    public IPage<GroupArticle> getPage(Pageable pageable, String keyword) {
        Page<GroupArticle> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<GroupArticle> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("name", keyword)
                    .or()
                    .like("description", keyword);
        }
        return groupArticleMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<GroupArticle> getPageByUserId(Pageable pageable, String keyword, Integer userId) {
        Page<GroupArticle> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<GroupArticle> wrapper = new QueryWrapper<GroupArticle>()
                .select("id", "name", "description")
                .eq("user_id", userId);
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("name", keyword);
        }
        wrapper.orderByDesc("update_time");
        return groupArticleMapper.selectPage(page, wrapper);
    }

}
