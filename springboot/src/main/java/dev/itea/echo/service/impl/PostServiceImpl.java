package dev.itea.echo.service.impl;

import dev.itea.echo.entity.Post;
import dev.itea.echo.mapper.PostMapper;
import dev.itea.echo.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 帖子表 服务实现类
 * </p>
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

}
