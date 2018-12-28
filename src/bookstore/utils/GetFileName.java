package bookstore.utils;

import java.util.UUID;

/**
 *
 * 获取文件夹的名字并且随机获取
 */
public class GetFileName {
    public static String makeFileName(String filename) {
        String ext = filename.substring(filename.lastIndexOf(".") + 1);//lastIndexOf("\\.")这样写不行
        return UUID.randomUUID().toString() + "." + ext;
    }
}
