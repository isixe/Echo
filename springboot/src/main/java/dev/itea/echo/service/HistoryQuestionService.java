package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.HistoryQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.HistoryQuestionVO;
import org.springframework.data.domain.Pageable;

/**
 * 用户问题浏览历史表 服务类
 *
 * @author isixe
 * @since 2024-03-18
 */
public interface HistoryQuestionService extends IService<HistoryQuestion> {

    void delete(Integer id);

    HistoryQuestion update(HistoryQuestion historyQuestion);

    HistoryQuestion get(Integer id);

    IPage<HistoryQuestionVO> getPageByUserId(Pageable pageable, String keyword, Integer userId);
}
