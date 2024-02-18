package dev.itea.echo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.DigestUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * UploadFileUtil
 *
 * @author: isixe
 * @create: 2024-02-01 19:43
 **/
@Slf4j
@SuppressWarnings("ResultOfMethodCallIgnored")
public class UploadFileUtil {

    private final Environment environment;
    private final static String RESOURCE_PATH = "src/main/resources";
    private final static String UPLOADED_FOLDER = "static";

    public UploadFileUtil(Environment environment) {
        this.environment = environment;
    }

    public String uploadFile(MultipartFile file, String dir) {
        try {
            String port = environment.getProperty("server.port");
            String appName = environment.getProperty("spring.application.name");
            String mode = environment.getProperty("spring.profiles.active");

            String localHost = "dev".equals(mode) || "test".equals(mode) ? "127.0.0.1" : new HostUtil().getLocalHostExactAddress();

            //target/upload
            String classpath = ResourceUtils.getURL("classpath:").getPath().substring(1);
            Path targetPath = Paths.get(classpath, UPLOADED_FOLDER);
            targetPath = Paths.get(targetPath.toString(), dir);

            // resources/upload
            Path resourcesPath = Paths.get(System.getProperty("user.dir"), appName);
            resourcesPath = Paths.get(resourcesPath.toString(), RESOURCE_PATH);
            resourcesPath = Paths.get(resourcesPath.toString(), UPLOADED_FOLDER);
            resourcesPath = Paths.get(resourcesPath.toString(), dir);

            //create path
            File targetExist = new File(targetPath.toString());
            File resourcesExist = new File(resourcesPath.toString());
            if (!targetExist.exists()) {
                targetExist.mkdirs();
                resourcesExist.mkdirs();
            }

            // suffix
            String originalFilename = file.getOriginalFilename();
            String fileSuffix = StringUtils.getFilenameExtension(originalFilename);

            //get full filename
            byte[] bytes = DigestUtils.md5Digest(file.getBytes());
            String md5 = new BigInteger(1, bytes).toString(16);
            String fileName = md5 + "." + fileSuffix;

            //save file
            File saveFile = new File(targetPath.toString(), fileName);
            if (!saveFile.exists()) {
                file.transferTo(saveFile);
                Files.copy(saveFile.toPath(), Paths.get(resourcesPath.toString(), fileName));
            }

            //join path
            String path = Paths.get(dir, fileName).toString();
            path = path.replace("\\", "/");
            return String.format("http://%s:%s/%s", localHost, port, path);
        } catch (Exception e) {
            log.error(e.toString());
            return "";
        }
    }
}
