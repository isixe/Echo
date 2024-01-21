package dev.itea.echo.service.impl;

import dev.itea.echo.entity.CommentArticle;
import dev.itea.echo.mapper.CommentArticleMapper;
import dev.itea.echo.service.CommentArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 文章评论表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class CommentArticleServiceImpl extends ServiceImpl<CommentArticleMapper, CommentArticle> implements CommentArticleService {

}
