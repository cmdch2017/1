package util;

<<<<<<< HEAD
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static final String DATE_PATTERN="yyyy-MM-dd HH:mm:ss";

    public static String parseSize(long size) {
        String[] danweis={"B","KB","MB","GB"};
        int index=0;
        while(size>1024&&index<danweis.length-1){
            size/=1024;
            index++;
        }
        return size+danweis[index];
    }

    public static String parseDate(Date lastModified) {
        return new SimpleDateFormat(DATE_PATTERN).format(lastModified);
    }

    public static void main(String[] args) {
        //TODO
        //1024-2047B的话误差感觉有点大,但是考虑到一个文件夹里就一个word，这种细节就忽略了
        System.out.println(parseSize(1023+1024));
        System.out.println(parseSize(10000));
    }
=======
public class Util {
    public static final String DATE_PATTERN="yyyy-MM-dd";
>>>>>>> c9db452291d2eb63072f2c5e2f6ac1de5ebf7d73
}
