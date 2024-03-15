package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.GroupArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

/**
 * 文章分组表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface GroupArticleService extends IService<GroupArticle> {

    void delete(Integer id);

    GroupArticle update(GroupArticle groupArticle);

    GroupArticle get(Integer id);

    GroupArticle getByUserIdAndGroupName(Integer userId, String groupName);

    IPage<GroupArticle> getPage(Pageable pageable, String keyword);

    IPage<GroupArticle> getPageByUserId(Pageable pageable, String keyword, Integer userId);
}
