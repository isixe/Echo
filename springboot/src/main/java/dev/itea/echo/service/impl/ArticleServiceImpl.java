package dev.itea.echo.service.impl;

import dev.itea.echo.entity.Article;
import dev.itea.echo.mapper.ArticleMapper;
import dev.itea.echo.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.UserRankVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    ArticleMapper articleMapper;

    @Override
    public List<UserRankVO> getUserArticleNumRankList() {
        return articleMapper.getUserArticleNumRankList();
    }
}
