package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.Category;
import dev.itea.echo.entity.GroupArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

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

    List<Map<String, Object>> getListByUserId(Integer userId);

    IPage<GroupArticle> getPage(Pageable pageable, String keyword);

}
