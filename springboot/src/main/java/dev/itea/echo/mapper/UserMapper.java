package dev.itea.echo.mapper;

import dev.itea.echo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.UserVO;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface UserMapper extends BaseMapper<User> {
    List<UserVO> getUserListByName(String name);
}
