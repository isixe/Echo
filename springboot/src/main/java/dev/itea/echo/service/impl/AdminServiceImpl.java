package dev.itea.echo.service.impl;

import dev.itea.echo.entity.Admin;
import dev.itea.echo.mapper.AdminMapper;
import dev.itea.echo.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 管理员表 服务实现类
 *
 * @author isixe
 * @since 2024-01-20
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
