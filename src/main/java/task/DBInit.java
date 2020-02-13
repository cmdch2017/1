package task;

import util.DBUtil;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

//初始化数据库，调用DBUtil.getConnection()就可以完成数据库初始化
// 读取sql文件
// 在执行sql语句来初始化表
public class DBInit {
    public static String[] readSQL(){
        try {
            //通过ClassLoader获取流，或者通过FileReader
            InputStream is=DBInit.class.getClassLoader()
                    .getResourceAsStream("init.sql");
            //字节流转换为字符流；需要通过字节字符转换流来实现
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(is,"UTF-8"));
            StringBuilder sb=new StringBuilder();
            String line;
            while((line=br.readLine())!=null){
                if(line.contains("--")){//取出--注释的代码
                    line=line.substring(0,line.indexOf("--"));
                }
                sb.append(line);
            }
            String[] sqls=sb.toString().split(";");
            return sqls;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取sql文件错误",e);
        }
    }

    public static void init(){
        Connection connection=null;
        Statement statement=null;
        try {
            //建立数据库连接
            connection= DBUtil.getConnection();
            //创建sql语句执行对象Statement
            statement=connection.createStatement();
            String[] sqls=readSQL();
            for(String sql:sqls){
                //执行sql语句
                statement.executeUpdate(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("初始化数据库表操作失败");
        }
        finally {
            DBUtil.close(connection,statement);
        }
        }
    public static void main(String[] args){
        System.out.println(Arrays.toString(readSQL()));
        init();
    }
}
