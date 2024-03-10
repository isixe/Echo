package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 类别表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface CategoryService extends IService<Category> {

    void delete(Integer id);

    Category update(Category category);

    Category get(Integer id);

    IPage<Category> getPage(Pageable pageable, String keyword);

    IPage<Category> getPageByName(Pageable pageable, String keyword);
}
