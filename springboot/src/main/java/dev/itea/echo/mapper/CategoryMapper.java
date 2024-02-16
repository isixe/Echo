package dev.itea.echo.mapper;

import dev.itea.echo.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.CategoryVO;

import java.util.List;

/**
 * <p>
 * 类别表 Mapper 接口
 * </p>
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CategoryMapper extends BaseMapper<Category> {
    List<CategoryVO> getCategotyListByName(String categoryName);
}
