package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.Category;
import dev.itea.echo.mapper.CategoryMapper;
import dev.itea.echo.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

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
    @CacheEvict(cacheNames = "category", key = "#id")
    public void delete(Integer id) {
        categoryMapper.deleteById(id);
    }

    @Override
    @CachePut(cacheNames = "category", key = "#category.id")
    public Category update(Category category) {
        categoryMapper.updateById(category);
        return category;
    }

    @Override
    @Cacheable(cacheNames = "category", key = "#id")
    public Category get(Integer id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public IPage<Category> getPage(Pageable pageable, String keyword) {
        Page<Category> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("category_name", keyword);
        }
        return categoryMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<Category> getPageByName(Pageable pageable, String keyword, String categoryName) {
        Page<Category> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<Category> wrapper = new QueryWrapper<Category>()
                .select("id", "category_name AS categoryName");
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("category_name", keyword);
        }
        return categoryMapper.selectPage(page, wrapper);
    }
}
