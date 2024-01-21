package dev.itea.echo.service.impl;

import dev.itea.echo.entity.CommentPost;
import dev.itea.echo.mapper.CommentPostMapper;
import dev.itea.echo.service.CommentPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 帖子评论表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class CommentPostServiceImpl extends ServiceImpl<CommentPostMapper, CommentPost> implements CommentPostService {

}
