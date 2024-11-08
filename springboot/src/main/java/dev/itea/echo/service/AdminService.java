package dev.itea.echo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import dev.itea.echo.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

/**
 * 管理员表 服务类
 *
 * @author isixe
 * @since 2024-01-20
 */
public interface AdminService extends IService<Admin> {

    void delete(Integer id);

    Admin update(Admin admin);

    Admin get(Integer id);

    IPage<Admin> getPage(Pageable pageable,String keword);
}
