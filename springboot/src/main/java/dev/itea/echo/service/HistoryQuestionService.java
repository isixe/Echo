package dev.itea.echo.service;

import dev.itea.echo.entity.HistoryQuestion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户问题浏览历史表 服务类
 *
 * @author isixe
 * @since 2024-03-18
 */
public interface HistoryQuestionService extends IService<HistoryQuestion> {

    void delete(Integer id);

    HistoryQuestion get(Integer id);

}
