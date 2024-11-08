package dev.itea.echo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dev.itea.echo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dev.itea.echo.vo.UserVO;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表 Mapper 接口
 *
 * @author isixe
 * @since 2024-01-15
 */
public interface UserMapper extends BaseMapper<User> {

    IPage<UserVO> getPage(Page<User> page, @Param(Constants.WRAPPER) QueryWrapper<UserVO> wrapper);

    IPage<UserVO> getPageWithFollowUserByUserId(Page<User> page, @Param(Constants.WRAPPER) QueryWrapper<UserVO> wrapper);

    IPage<UserVO> getPageWithFollowerUserByUserId(Page<User> page, @Param(Constants.WRAPPER) QueryWrapper<UserVO> wrapper);
}
