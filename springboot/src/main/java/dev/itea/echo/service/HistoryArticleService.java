package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.HistoryArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.HistoryArticleVO;
import org.springframework.data.domain.Pageable;

/**
 * 用户文章浏览历史表 服务类
 *
 * @author isixe
 * @since 2024-03-18
 */
public interface HistoryArticleService extends IService<HistoryArticle> {

    void delete(Integer id);

    HistoryArticle update(HistoryArticle historyArticle);

    HistoryArticle get(Integer id);

    IPage<HistoryArticleVO> getPageByUserId(Pageable pageable, String keyword, Integer userId);
}
