package dev.itea.echo.service.impl;

import dev.itea.echo.entity.Follow;
import dev.itea.echo.mapper.FollowMapper;
import dev.itea.echo.service.FollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author isixe
 * @since 2024-03-13
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Resource
    FollowMapper followMapper;

    @Override
    @CacheEvict(cacheNames = "follow", key = "#id")
    public void delete(Integer id) {
        followMapper.deleteById(id);
    }


    @Override
    @Cacheable(cacheNames = "follow", key = "#id")
    public Follow get(Integer id) {
        return followMapper.selectById(id);
    }
}
