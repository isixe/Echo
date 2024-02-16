package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.UserVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 用户表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface UserService extends IService<User> {
    List<UserVO> getUserListByName(String userName);

    IPage<User> getUserByPage(Pageable pageable, String keword);
}
