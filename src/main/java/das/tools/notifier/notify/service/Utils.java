package das.tools.notifier.notify.service;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.StringJoiner;

/**
 * Utility class
 */
public class Utils {
    /**
     * @param inStr string to be linearized
     * @return linearized string w/o EOLs
     */
    public static String linearizedString(Object inStr){
        if (inStr == null) return "";
        return inStr.toString().replaceAll("\n", "\\n");
    }
    public static String getNotNullString(String inStr) {
        return getNotNullStringOrDefault(inStr, "");
    }
    public static String getNotNullStringOrDefault(String inStr, String def){
        return inStr != null ? inStr : def;
    }

    /**
     * @param fileName path to file
     * @return MIME type of file
     */
    public static String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        fileName = (new File(fileName)).getName();
        return fileNameMap.getContentTypeFor(fileName);
    }

    /**
     * @param fileName path to file
     * @return true if fileName - it's an image file
     */
    public static boolean isImageFile(String fileName) {
        return "image".equals(getUpType(fileName));
    }
    /**
     * @param fileName path to file
     * @return true if fileName - it's an video file
     */
    public static boolean isVideoFile(String fileName) {
        return "video".equals(getUpType(fileName));
    }

    /**
     * @param s filename
     * @return first part of file's MIME type (image/png -> image)
     */
    private static String getUpType(String s) {
        String mimeType = getMimeType(s);
        return mimeType.split("/")[0];
    }

    /**
     * @param inStr input string
     * @return input string with removed HTML tags
     */
    public static String removeHtmlTags(String inStr) {
        return (inStr != null && !"".equals(inStr)) ? inStr.replaceAll("<[^>]*>", "") : inStr;
    }

    /**
     * @param fileName filename in current or sub Application's directory
     * @return absolute file name to given file
     */
    public static String getDestFilePath(String fileName) {
        StringJoiner localFilePath = new StringJoiner(File.separator);
        localFilePath.add(System.getProperty("user.dir"))
                .add(Sender.FILES_RELATIVE_PATH)
                .add(fileName);
        return localFilePath.toString();
    }

    /**
     * @param fileName filename
     * @return extension of given file
     */
    public static String getFileExtension(String fileName){
        int p = fileName.lastIndexOf(".");
        return p > 0 ? fileName.substring(p) : "";
    }
}
