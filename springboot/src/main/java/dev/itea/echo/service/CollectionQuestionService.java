package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.CollectionQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.CollectionQuestionVO;
import org.springframework.data.domain.Pageable;

/**
 * 收藏表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CollectionQuestionService extends IService<CollectionQuestion> {

    CollectionQuestion get(Integer id);

    void delete(Integer id);

    IPage<CollectionQuestionVO> getCollectionQuestionByPage(Pageable pageable, String keyword);

    IPage<CollectionQuestionVO> getCollectionQuestionPageByUserId(Pageable pageable, Integer userId);
}
