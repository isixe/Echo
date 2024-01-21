package dev.itea.echo.service.impl;

import dev.itea.echo.entity.Category;
import dev.itea.echo.mapper.CategoryMapper;
import dev.itea.echo.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 类别表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
