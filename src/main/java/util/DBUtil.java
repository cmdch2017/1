package util;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import task.DBInit;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static volatile DataSource DATA_SOURCE;

    private static DataSource getDataSource(){
        if(DATA_SOURCE==null){
            synchronized (DBUtil.class){
                if(DATA_SOURCE==null){
                    //初始化操作
                    SQLiteConfig config=new SQLiteConfig();
                    config.setDateStringFormat(Util.DATE_PATTERN);
                    DATA_SOURCE=new SQLiteDataSource();
                    ((SQLiteDataSource)DATA_SOURCE).setUrl(getUrl());
                }
            }
        }
        return DATA_SOURCE;
    }
    //获取sqlite数据库文件url的方法
    private static String getUrl(){
        //获取targer编译文件夹的路径
        //通过classLoader.getResource()/classLoader.getReSourceAsStream();
        //默认的根路径为编译文件夹路径
        URL classesURL= DBInit.class.getClassLoader().getResource("./");
        //获取target/classes文件夹的父目录路径
        String dir=new File(classesURL.getPath()).getParent();
        String url="jdbc:sqlite://"+dir+File.separator+"everything-like.db";
        System.out.println("获取数据库文件路径："+url);
        return url;
    }



    //从数据库连接池连接的方法
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(getConnection());
    }

    public static void close(Connection connection, Statement statement) {
        close(connection,statement,null);
    }

    //释放数据库资源
    //connection 数据库连接
    //statement sql执行对象
    //resultSet 结果集

    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if(connection!=null)
                connection.close();
            if(statement!=null)
                statement.close();
            if(resultSet!=null)
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("释放数据库资源失败",e);
        }
    }
}
