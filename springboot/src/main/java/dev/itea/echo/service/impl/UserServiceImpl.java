package dev.itea.echo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
    @CacheEvict(cacheNames = "user", key = "#user.id")
    public void updateProfile(User user) {
        userMapper.update(null, new UpdateWrapper<User>()
                .eq("id", user.getId())
                .set("name", user.getName())
                .set("email", user.getEmail())
                .set("description", user.getDescription())
                .set("avatar", user.getAvatar()));
    }

    @Override
    @Cacheable(cacheNames = "user", key = "#id")
    public User get(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public IPage<User> getPage(Pageable pageable, String keyword) {
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

    @Override
    public IPage<UserVO> getPageByName(Pageable pageable, String name) {
        Page<User> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<UserVO> wrapper = new QueryWrapper<UserVO>()
                .eq("u.is_deleted", 0)
                .like("name", name);
        return userMapper.getPage(page, wrapper);
    }

    @Override
    public IPage<UserVO> getPageWithFollowUserByUserId(Pageable pageable, Integer userId) {
        Page<User> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<UserVO> wrapper = new QueryWrapper<UserVO>()
                .eq("f.is_deleted", 0)
                .eq("u.is_deleted", 0)
                .eq("f.user_id", userId);
        return userMapper.getPageWithFollowUserByUserId(page, wrapper);
    }

    @Override
    public IPage<UserVO> getPageWithFollowerUserByUserId(Pageable pageable, Integer userId) {
        Page<User> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<UserVO> wrapper = new QueryWrapper<UserVO>()
                .eq("f.is_deleted", 0)
                .eq("u.is_deleted", 0)
                .eq("f.follow_user_id", userId);
        return userMapper.getPageWithFollowerUserByUserId(page, wrapper);
    }

}
