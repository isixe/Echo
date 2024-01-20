package dev.itea.echo.service.impl;

import dev.itea.echo.entity.Question;
import dev.itea.echo.mapper.QuestionMapper;
import dev.itea.echo.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 问答表 服务实现类
 * </p>
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

}
