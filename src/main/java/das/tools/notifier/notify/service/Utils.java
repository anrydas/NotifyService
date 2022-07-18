package das.tools.notifier.notify.service;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

public class Utils {
    public static String linearizedString(Object inStr){
        return inStr.toString().replaceAll("\n", "\\n");
    }
    public static String getNotNullString(String inStr) {
        return getNotNullStringOrDefault(inStr, "");
    }
    public static String getNotNullStringOrDefault(String inStr, String def){
        return inStr != null ? inStr : def;
    }

    public static String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        fileName = (new File(fileName)).getName();
        return fileNameMap.getContentTypeFor(fileName);
    }

    public static boolean isImageFile(String fileName) {
        return "image".equals(getUpType(fileName));
    }

    public static boolean isVideoFile(String fileName) {
        return "video".equals(getUpType(fileName));
    }

    private static String getUpType(String s) {
        String mimeType = getMimeType(s);
        return mimeType.split("/")[0];
    }

    public static String removeHtmlTags(String inStr) {
        return (inStr != null && !"".equals(inStr)) ? inStr.replaceAll("<[^>]*>", "") : inStr;
    }
}
