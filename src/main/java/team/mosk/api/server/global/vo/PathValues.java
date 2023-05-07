package team.mosk.api.server.global.vo;

public enum PathValues {
    BASIC_DIR_IMG("\\basic\\basic.jpg"),
    FILE_DIR("\\imgs\\"),
    BASIC_DIR("\\basic\\");

   private final String info;

    PathValues(String info) {
        this.info = info;
    }

    public String info() {
        return info;
    }
}
