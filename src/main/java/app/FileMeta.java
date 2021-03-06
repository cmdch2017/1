package app;

<<<<<<< HEAD
import util.PinyinUtil;
import util.Util;

import java.io.File;
import java.util.Date;
import java.util.Objects;

public class FileMeta {
    //文件名称
    private String name;
    //文件所在的父目录路径
    private String path;
    //文件大小
    private Long size;
    //文件上次修改时间
    private Date lastModified;
    //是否是文件夹
    private Boolean isDirectory;
    //给客户端控件使用，和app.fxml定义的名称要一致
    private String sizeText;
    //和app.fxml定义的一致
    private String lastModifiedText;
    private String pinyin;
    private String pinyinFirst;
    public FileMeta(File file){
        this(file.getName(),file.getParent(),file.isDirectory(),file.length(),new Date(file.lastModified()));
    }
    public FileMeta(String name,String path,Boolean isDirectory,long size,Date lastModified){
        this.name=name;
        this.path=path;
        this.isDirectory=isDirectory;
        this.size=size;
        this.lastModified=lastModified;
        if(PinyinUtil.containsChinese(name)){
            String[] pinyins=PinyinUtil.get(name);
            pinyin=pinyins[0];
            pinyinFirst=pinyins[1];
        }
        //TODO
        sizeText= Util.parseSize(size);
        lastModifiedText=Util.parseDate(lastModified);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", isDirectory=" + isDirectory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMeta fileMeta = (FileMeta) o;
        return Objects.equals(name, fileMeta.name) &&
                Objects.equals(path, fileMeta.path) &&
                Objects.equals(isDirectory, fileMeta.isDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, isDirectory);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public String getLastModifiedText() {
        return lastModifiedText;
    }

    public void setLastModifiedText(String lastModifiedText) {
        this.lastModifiedText = lastModifiedText;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinFirst() {
        return pinyinFirst;
    }

    public void setPinyinFirst(String pinyinFirst) {
        this.pinyinFirst = pinyinFirst;
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }
=======
public class FileMeta {
>>>>>>> c9db452291d2eb63072f2c5e2f6ac1de5ebf7d73
}
