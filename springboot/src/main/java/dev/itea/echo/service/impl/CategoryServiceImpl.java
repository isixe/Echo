package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.Category;
import dev.itea.echo.mapper.CategoryMapper;
import dev.itea.echo.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 类别表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @Override
    public IPage<Category> getCategoryByPage(Pageable pageable, String keyword) {
        Page<Category> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("category_name", keyword);
        }
        return categoryMapper.selectPage(page, wrapper);
    }
}
