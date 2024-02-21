package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.User;
import dev.itea.echo.mapper.UserMapper;
import dev.itea.echo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.itea.echo.vo.UserVO;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 用户表 服务实现类
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    @CacheEvict(cacheNames = "user", key = "#id")
    public void delete(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    @CachePut(cacheNames = "user", key = "#user.id")
    public User update(User user) {
        userMapper.updateById(user);
        return user;
    }

    @Override
    @Cacheable(cacheNames = "user", key = "#id")
    public User get(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<UserVO> getUserListByName(String userName) {
        return userMapper.getUserListByName(userName);
    }

    @Override
    public IPage<User> getUserByPage(Pageable pageable, String keyword) {
        Page<User> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(keyword)) {
            wrapper = wrapper.like("name", keyword)
                    .or()
                    .like("email", keyword)
                    .or()
                    .like("description", keyword);
        }
        return userMapper.selectPage(page, wrapper);
    }

}
