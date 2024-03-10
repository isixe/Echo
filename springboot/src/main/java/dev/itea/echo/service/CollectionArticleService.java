package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.CollectionArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.CollectionArticleVO;
import org.springframework.data.domain.Pageable;

/**
 * 收藏表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CollectionArticleService extends IService<CollectionArticle> {

    CollectionArticle get(Integer id);

    void delete(Integer id);

    IPage<CollectionArticleVO> getPage(Pageable pageable, String keyword);

    IPage<CollectionArticleVO> getPageByUserId(Pageable pageable, Integer userId);
}
