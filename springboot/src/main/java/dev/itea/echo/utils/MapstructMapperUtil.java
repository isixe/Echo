package dev.itea.echo.utils;

import dev.itea.echo.dto.RegisterDTO;
import dev.itea.echo.entity.Article;
import dev.itea.echo.entity.User;
import dev.itea.echo.vo.ArticleVO;
import dev.itea.echo.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct对象 Mapper 映射工具
 *
 * @author: isixe
 * @create: 2024-02-14 19:34
 **/
@Mapper
public interface MapstructMapperUtil {
    MapstructMapperUtil INSTANCE = Mappers.getMapper(MapstructMapperUtil.class);

    User registerDTOToUser(RegisterDTO registerDTO);

    UserVO userToUserVO(User user);

    Article articleVOToArticle(ArticleVO articleVO);
}
