package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dev.itea.echo.entity.UserPersonalize;
import dev.itea.echo.mapper.UserPersonalizeMapper;
import dev.itea.echo.service.UserPersonalizeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
 * 用户个性化设置表 服务实现类
 *
 * @author isixe
 * @since 2024-03-26
 */
@Service
public class UserPersonalizeServiceImpl extends ServiceImpl<UserPersonalizeMapper, UserPersonalize> implements UserPersonalizeService {

    @Resource
    UserPersonalizeMapper userPersonalizeMapper;

    @Override
    @Caching(put = {
            @CachePut(cacheNames = "userPersonalize", key = "#userPersonalize.id"),
    })
    public UserPersonalize update(UserPersonalize userPersonalize) {
        userPersonalizeMapper.updateById(userPersonalize);
        return userPersonalize;
    }

    @Override
    @Cacheable(cacheNames = "userPersonalize", key = "#id")
    public UserPersonalize get(Integer id) {
        return userPersonalizeMapper.selectById(id);
    }

    @Override
    public UserPersonalize getByUserId(Integer userId) {
        return userPersonalizeMapper.selectOne(new QueryWrapper<UserPersonalize>()
                .eq("user_id", userId));
    }

}
