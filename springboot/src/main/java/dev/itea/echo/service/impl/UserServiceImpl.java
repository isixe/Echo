package dev.itea.echo.service.impl;

import dev.itea.echo.entity.User;
import dev.itea.echo.mapper.UserMapper;
import dev.itea.echo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author isixe
 * @since 2024-01-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
