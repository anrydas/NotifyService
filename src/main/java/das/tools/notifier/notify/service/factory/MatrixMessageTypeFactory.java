package das.tools.notifier.notify.service.factory;

public class MatrixMessageTypeFactory {
    public static String get(String mimeType, String url) {
        String type = mimeType.split("/")[0];
        String res = "m.text";
        if ("image".equals(type)) {
            res = "m.image";
        } else if ("audio".equals(type)) {
            res = "m.audio";
        } else if ("video".equals(type)) {
            res = "m.video";
        } else if (url != null) {
            res = "m.file";
        }
        return res;
    }
}
