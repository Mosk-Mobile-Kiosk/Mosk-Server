package team.mosk.api.server.global.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import team.mosk.api.server.global.vo.NowProjectPathIs;
import team.mosk.api.server.global.vo.PathValues;

import static team.mosk.api.server.global.vo.PathValues.*;

@Component
public class GetPathByEnvironment {
    public static String getPath(final String mode) {
        String ENV_LOCAL_PATH = NowProjectPathIs.getPath();

        String isLocated = ENV_LOCAL_PATH.substring(0, ENV_LOCAL_PATH.lastIndexOf("\\"));

        if (mode.equals("basic")) {
            return isLocated + BASIC_DIR_IMG.info();
        } else if (mode.equals("update")) {
            return isLocated + FILE_DIR.info();
        }

        return null;
    }
}
