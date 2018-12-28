package bookstore.utils;

import java.util.UUID;

/**
 * 随机生成字符串
 */
public class StringUtils {
    /**
     * 没有中间的字符
     * @return
     */
    public static String gengraString(){
      return   UUID.randomUUID().toString().replace("-","");
    }
    /**
     * 有中间的字符
     * @return
     */
    public static String gengraStringWith(){
        return   UUID.randomUUID().toString();
    }
}
