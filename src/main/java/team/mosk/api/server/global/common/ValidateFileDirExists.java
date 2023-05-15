package team.mosk.api.server.global.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import team.mosk.api.server.global.vo.NowProjectPathIs;
import team.mosk.api.server.global.vo.PathValues;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
@Profile("windows")
@Slf4j
public class ValidateFileDirExists {
    private static final String ALREADY_EXIST_DIR_OR_ERROR = "이미 디렉토리가 존재하거나 생성에 실패했습니다.";

    @PostConstruct
    public void validateDir() {
        log.info("validateDir has starting work");
        String ORIGIN_PATH = NowProjectPathIs.getPath();

        String basicPath
                = ORIGIN_PATH.substring(0, ORIGIN_PATH.lastIndexOf("\\")) + PathValues.BASIC_DIR.info();

        String filePath
                = ORIGIN_PATH.substring(0, ORIGIN_PATH.lastIndexOf("\\")) + PathValues.FILE_DIR.info();

        File basicPathDir = new File(basicPath);
        boolean firResult = createDirIfNotExist(basicPathDir);
        if (!firResult) {
            log.info("ValidateFileDirExists is throws message [{}]", ALREADY_EXIST_DIR_OR_ERROR);
        } else {
            log.info("ValidateFileDirExists is created new directory");
        }

        File filePathDir = new File(filePath);
        boolean secResult = createDirIfNotExist(filePathDir);
        if (!secResult) {
            log.info("ValidateFileDirExists is throws message [{}]", ALREADY_EXIST_DIR_OR_ERROR);
        } else {
            log.info("ValidateFileDirExists is created new directory");
        }
    }

    public boolean createDirIfNotExist(final File dir) {
        if (dir.exists() && dir.isDirectory()) {
            return false;
        } else return dir.mkdirs();
    }
}
