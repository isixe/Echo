package dev.itea.echo.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckOr;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.dto.PageDTO;
import dev.itea.echo.entity.Category;
import dev.itea.echo.entity.result.ResultCode;
import dev.itea.echo.exception.BusinessException;
import dev.itea.echo.service.CategoryService;
import dev.itea.echo.utils.StpUserUtil;
import dev.itea.echo.validation.AddValidationGroup;
import dev.itea.echo.validation.UpdateValidationGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 类别控制器
 *
 * @author isixe
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 类别新增
     *
     * @param category 类别实体
     */
    @Operation(summary = "类别新增", description = "后台类别新增", tags = "Category", method = "POST",
            parameters = {
                    @Parameter(name = "category", description = "类别实体", required = true),
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @PostMapping
    public void add(@Validated(AddValidationGroup.class) Category category) {
        //check category
        Category checkCategory = categoryService.getOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, category.getCategoryName()));
        if (!ObjectUtils.isEmpty(checkCategory)) {
            throw new BusinessException(ResultCode.DATA_ALREADY_EXISTED);
        }
        categoryService.save(category);
    }

    /**
     * 类别更新
     *
     * @param category 类别实体
     */
    @Operation(summary = "类别更新", description = "后台类别更新", tags = "Category", method = "PUT",
            parameters = {
                    @Parameter(name = "category", description = "类别实体", required = true),
            })
    @SaCheckLogin
    @PutMapping
    public void update(@Validated(UpdateValidationGroup.class) Category category) {
        //check category
        Category checkCategory = categoryService.get(category.getId());
        if (ObjectUtils.isEmpty(checkCategory)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //update
        categoryService.update(category);
    }

    /**
     * 类别删除
     *
     * @param id 类别ID
     */
    @Operation(summary = "类别删除", description = "后台类别删除", tags = "Category", method = "DELETE",
            parameters = {
                    @Parameter(name = "id", description = "类别ID", required = true, example = "2"),
            })
    @SaCheckLogin
    @DeleteMapping
    public void delete(Integer id) {
        //check category
        Category checkCategory = categoryService.get(id);
        if (ObjectUtils.isEmpty(checkCategory)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        //delete
        categoryService.delete(id);
    }

    /**
     * 类别查询（ID）
     *
     * @param id 类别ID
     */
    @Operation(summary = "类别查询", description = "后台类别查询", tags = "Category", method = "GET",
            parameters = {
                    @Parameter(name = "id", description = "类别ID", required = true, example = "2"),
            })
    @SaIgnore
    @GetMapping
    public Category getById(Integer id) {
        //get category
        Category category = categoryService.get(id);
        //check category
        if (ObjectUtils.isEmpty(category)) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND);
        }
        return category;
    }

    /**
     * 类别查询（分页&关键词）
     *
     * @param pageDTO 分页数据传输对象
     * @return IPage 分页对象
     */
    @Operation(summary = "类别分页与关键词查询", description = "后台类别分页与关键词查询", tags = "Category", method = "GET",
            parameters = {
                    @Parameter(name = "pageDTO", description = "分页数据传输对象", required = true)
            })
    @SaIgnore
    @GetMapping("/queryAll")
    public IPage<Category> getPageByKeyword(@Validated PageDTO pageDTO) {
        Pageable pageable = PageRequest.of(pageDTO.getPageNum(), pageDTO.getPageSize());
        return categoryService.getPage(pageable, pageDTO.getKeyword());
    }

    /**
     * 类别名称查询（Name）
     *
     * @param categoryName 类别名称
     * @return category map
     */
    @Operation(summary = "类别名称查询（Name）", description = "类别名称模糊查询", tags = "Category", method = "GET",
            parameters = {
                    @Parameter(name = "categoryName", description = "类别名称关键词", required = true, example = "类别")
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @GetMapping("/queryByName")
    public Category getByName(String categoryName) {
        return categoryService.getOne(new QueryWrapper<Category>()
                .eq("category_name", categoryName));
    }

    /**
     * 类别模糊查询（Name）
     *
     * @param categoryName 类别名称
     * @return category map
     */
    @Operation(summary = "类别模糊查询（Name）", description = "类别名称模糊查询", tags = "Category", method = "GET",
            parameters = {
                    @Parameter(name = "categoryName", description = "类别名称关键词", required = true, example = "类别")
            })
    @SaCheckOr(
            login = {@SaCheckLogin, @SaCheckLogin(type = StpUserUtil.TYPE)}
    )
    @GetMapping("/queryAllByName")
    public List<Map<String, Object>> getListByName(String categoryName) {
        return categoryService.getListByName(categoryName);
    }

}
