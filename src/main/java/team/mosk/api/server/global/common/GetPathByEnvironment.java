package team.mosk.api.server.global.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class GetPathByEnvironment {

    private static final String BASIC_DIR_AND_FILE = "\\basic\\basic.jpg";
    private static final String FILE_DIR = "\\imgs\\";

    private static final String ENV_LOCAL_PATH = System.getProperty("user.dir");


    public static String getPath(final String mode) {
        String isLocated = ENV_LOCAL_PATH.substring(0, ENV_LOCAL_PATH.lastIndexOf("\\"));

        if (mode.equals("basic")) {
            return isLocated + BASIC_DIR_AND_FILE;
        } else if (mode.equals("update")) {
            return isLocated + FILE_DIR;
        }

        return null;
    }
}
