package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import dev.itea.echo.vo.UserVO;
import org.springframework.data.domain.Pageable;

/**
 * 用户表 服务类
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface UserService extends IService<User> {

    void delete(Integer id);

    User update(User user);

    void updateProfile(User user);

    User get(Integer id);

    IPage<User> getPage(Pageable pageable, String keword);

    IPage<UserVO> getPageByName(Pageable pageable, String keyword);

    IPage<UserVO> getPageWithFollowUserByUserId(Pageable pageable, Integer userId);

    IPage<UserVO> getPageWithFollowerUserByUserId(Pageable pageable, Integer userId);

}
