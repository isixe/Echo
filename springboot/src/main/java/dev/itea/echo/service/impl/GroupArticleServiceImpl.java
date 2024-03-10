package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.GroupArticle;
import dev.itea.echo.mapper.GroupArticleMapper;
import dev.itea.echo.service.GroupArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.ArticleVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> getListByUserId(Integer userId) {
        QueryWrapper<GroupArticle> wrapper = new QueryWrapper<>();
        wrapper = wrapper.select("id", "name", "description")
                .eq("user_id", userId)
                .orderByDesc("update_time");
        return groupArticleMapper.selectMaps(wrapper);
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
    public GroupArticle getByUserIdAndGroupName(Integer userId, String groupName) {
        return groupArticleMapper.selectOne(new QueryWrapper<GroupArticle>()
                .eq("user_id", userId)
                .eq("name", groupName));
    }

}
