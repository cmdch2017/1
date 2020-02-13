package task;

import app.FileMeta;
import util.DBUtil;
import util.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import java.util.List;

public class FileSave implements  ScanCallback{
    @Override
    public void callback(File dir) {
        //文件夹下一级子文件和子文件夹保存到数据库
        File[] children=dir.listFiles();
        List<FileMeta> locals=new ArrayList<>();
        if(children!=null){
            for(File child:children){
                locals.add(new FileMeta(child));
            }
        }
        //获取数据库保存的dir木的下一级子文件和子文件夹
        //TODO LIST <File>
        //本地有，数据库没有，做插入
        List<FileMeta> metas=query(dir);
        for(FileMeta meta:metas){
            if(!locals.contains(meta)){
                //meta的删除:
                // 1.删除meta信息本身
                // 2.如果meta是一个目录，还要将meta的所有子文件和子文件夹都删除
                //TODO delete
                delete(meta);
            }
        }
        for(FileMeta meta:locals){
            if(!metas.contains(meta)){
                save(meta);
            }
        }
        //TODO
        //数据库有，本地没有，做删除
    }
    private List<FileMeta> query(File dir){
        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<FileMeta> metas=new ArrayList<>();
        try{
            //1.创建数据库连接
            connection=DBUtil.getConnection();
            String sql="select name,path,is_directory,size,last_modified"+
                    " from file_meta where path=?";
            //2.创建jdbc操作命令对象statement
            ps=connection.prepareStatement(sql);
            ps.setString(1,dir.getPath());
            //3.执行sql语句
            rs=ps.executeQuery();
            //4.处理结果集ResultSet
            while(rs.next()){
                String name=rs.getString("name");
                String path=rs.getString("path");
                Boolean isDirectory=rs.getBoolean("is_directory");
                Long size=rs.getLong("size");
                Timestamp lastModified=rs.getTimestamp("last_modified");
                FileMeta meta=new FileMeta(name,path,isDirectory,size,new java.util.Date(lastModified.getTime()));
                System.out.printf("查询文件信息: name=%s,path=%s,is_directory=%s,"+
                        " size=%s,last_modified=%s\n",name,path,String.valueOf(isDirectory),String.valueOf(size),
                        Util.parseDate(new java.util.Date(lastModified.getTime()))
                        );
                metas.add(meta);
            }
            return metas;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("查询文件信息出错,检查sql查询语句",e);
        }finally {
            DBUtil.close(connection,ps,rs);
        }
    }

    private void save(FileMeta meta){
        Connection connection=null;
        PreparedStatement statement=null;
        try {
            //1.获取数据库连接
            connection=DBUtil.getConnection();
            String sql="insert into file_meta"+
                    "(name,path,is_directory,size,last_modified,pinyin,pinyin_first)"+
                    " values(?,?,?,?,?,?,?)";
            //2.获取sql操作命令对象statement
            statement=connection.prepareStatement(sql);
            statement.setString(1,meta.getName());
            statement.setString(2,meta.getPath());
            statement.setBoolean(3,meta.getDirectory());
            statement.setLong(4,meta.getSize());
           //数据库保存日期类型，可以按数据库设置的日期格式，以字符串传入
            statement.setString(5,meta.getLastModifiedText());
            statement.setString(6,meta.getPinyin());
            statement.setString(7,meta.getPinyinFirst());
         /* String pinyin=null;
            String pinyin_first=null;
            //文件名包含汉字，需要获取拼音和汉字字母
           if(PinyinUtil.containsChinese(file.getName())){
                String[] pinyins=PinyinUtil.get(file.getName());
                pinyin=pinyins[0];
                pinyin_first=pinyins[1];
            }
            */

            System.out.println("执行文件保存操作："+sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("文件保存失败,检查sql insert语句",e);
        }finally{
            DBUtil.close(connection,statement);
        }
    }

    public void delete(FileMeta meta){
        Connection connection=null;
        PreparedStatement ps=null;
        try{
            connection=DBUtil.getConnection();
            String sql="delete from file_meta where"+
                    " (name=? and path=? and is_directory=?)";//删除文件自身
            if(meta.getDirectory()){//如果是文件夹，还要删除子文件夹
                sql+=" or path=?" +//匹配数据库文件夹的儿子
                     " or path like ?";//匹配数据库文件夹的孙子辈
            }
            ps =connection.prepareStatement(sql);
            ps.setString(1,meta.getName());
            ps.setString(2,meta.getPath());
            ps.setBoolean(3,meta.getDirectory());
            if(meta.getDirectory()){
                ps.setString(4,
                        meta.getPath()+File.separator+meta.getName());
                ps.setString(5,
                        meta.getPath()+File.separator+meta.getName()+File.separator);
            }
            System.out.printf("删除文件信息,dir=%s\n",
                    meta.getPath()+File.separator+meta.getName());
            ps.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException("删除文件信息出错,检查delete语句");
        }finally {
            DBUtil.close(connection,ps);
        }
        }


    public static void main(String[] args) {
       /* DBInit.init();
        File file=new File("D:\\bitekeji");
        FileSave fileSave=new FileSave();
        fileSave.save(file);
        fileSave.query(file.getParentFile());*/
       List<FileMeta> locals=new ArrayList<>();
       locals.add(new FileMeta("新建文件夹","D:\\bitekeji\\maven-test - 副本",true,0,new Date()));
       locals.add(new FileMeta("中华人民共和国","D:\\bitekeji\\maven-test - 副本",true,0,new Date()));
       locals.add(new FileMeta("阿凡达.txt","D:\\bitekeji\\maven-test - 副本\\中华人民共和国",true,0,new Date()));

        List<FileMeta> metas=new ArrayList<>();
        metas.add(new FileMeta("新建文件夹","D:\\bitekeji\\maven_test - 副本",true,0,new Date()));
        metas.add(new FileMeta("中华人民共和国2","D:\\bitekeji\\maven_test - 副本",true,0,new Date()));
        metas.add(new FileMeta("阿凡达.txt","D:\\bitekeji\\maven_test - 副本\\中华人民共和国2",true,0,new Date()));
        Boolean contains=locals.contains(new FileMeta(new File("")));
        //集合中是否包含某个元素，不一定代表传入的这个对象在java中是同一个引用
        //满足一定条件(集合中的元素类型需要重写hashCode和equals)，list.contains为true
        //List.contains方法可以返回true
        for(FileMeta meta:locals){
            if(!metas.contains(meta)){
                System.out.println(meta);
            }
        }
    }
}
