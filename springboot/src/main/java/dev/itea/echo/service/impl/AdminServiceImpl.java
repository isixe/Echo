package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.Admin;
import dev.itea.echo.mapper.AdminMapper;
import dev.itea.echo.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 管理员表 服务实现类
 *
 * @author isixe
 * @since 2024-01-20
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Resource
    AdminMapper adminMapper;

    @Override
    @CacheEvict(cacheNames = "admin", key = "#id")
    public void delete(Integer id) {
        adminMapper.deleteById(id);
    }

    @Override
    @CachePut(cacheNames = "admin", key = "#admin.id")
    public Admin update(Admin admin) {
        adminMapper.updateById(admin);
        return admin;
    }

    @Override
    @Cacheable(cacheNames = "admin", key = "#id")
    public Admin get(Integer id) {
        return adminMapper.selectById(id);
    }

    @Override
    public IPage<Admin> getPage(Pageable pageable, String keyword) {
        Page<Admin> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("name", keyword).or().like("email", keyword);
        }
        return adminMapper.selectPage(page, wrapper);
    }
}
