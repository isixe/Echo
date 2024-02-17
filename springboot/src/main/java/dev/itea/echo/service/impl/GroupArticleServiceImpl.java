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
    public List<Map<String, Object>> getGroupArticleByUserId(Integer userId) {
        return groupArticleMapper.selectMaps(new QueryWrapper<GroupArticle>()
                .select("id", "name", "description")
                .eq("user_id", userId));
    }

    @Override
    public IPage<GroupArticle> getGroupArticleByPage(Pageable pageable, String keyword) {
        Page<GroupArticle> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<GroupArticle> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("name", keyword)
                    .or()
                    .like("description", keyword);
        }
        return groupArticleMapper.selectPage(page, wrapper);
    }

}
