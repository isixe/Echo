package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.CollectionQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.CollectionQuestionVO;
import org.apache.ibatis.annotations.Param;

/**
 * 收藏表 Mapper 接口
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CollectionQuestionMapper extends BaseMapper<CollectionQuestion> {

    IPage<CollectionQuestionVO> getCollectionArticleByPage(Page<CollectionQuestionVO> page, @Param(Constants.WRAPPER)QueryWrapper<CollectionQuestionVO> wrapper);
}
