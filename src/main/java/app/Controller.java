package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
<<<<<<< HEAD
import task.*;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.List;
=======
import task.DBInit;

import java.io.File;
import java.net.URL;
>>>>>>> c9db452291d2eb63072f2c5e2f6ac1de5ebf7d73
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private GridPane rootPane;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<FileMeta> fileTable;

    @FXML
    private Label srcDirectory;

<<<<<<< HEAD
    private Thread task;

=======
>>>>>>> c9db452291d2eb63072f2c5e2f6ac1de5ebf7d73
    public void initialize(URL location, ResourceBundle resources) {
        //界面初始化时，需要初始化数据库及表
        // 添加搜索框监听器，内容改变时执行监听事件
        DBInit.init();
        searchField.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                freshTable();
            }
        });
    }
<<<<<<< HEAD
    //点击选择“目录”按钮显示
=======

>>>>>>> c9db452291d2eb63072f2c5e2f6ac1de5ebf7d73
    public void choose(Event event) {
        // 选择文件目录
        DirectoryChooser directoryChooser=new DirectoryChooser();
        Window window = rootPane.getScene().getWindow();
        File file = directoryChooser.showDialog(window);
        if(file == null)
            return;
        // 获取选择的目录路径，并显示
        String path = file.getPath();
        // TODO
<<<<<<< HEAD
        srcDirectory.setText(path);
        if(task!=null){
            task.interrupt();//初始状态(中止状态)也会中断
        }
        //选择了目录，就需要执行目录的扫描任务：该目录所以的子文件和子文件夹都扫描出来
        task=new Thread(new Runnable() {
            @Override
            public void run() {
                ScanCallback callback=new FileSave();//文件扫描回调接口，做文件夹下一级子文件和子文件夹保存数据库的内容
                FileScanner scanner = new FileScanner(callback);//为了提高效率多线程来执行文件扫描任务
                try {
                    System.out.println("执行文件扫描任务");
                    scanner.scan(path);
                    scanner.waitFinish();
                    System.out.println("任务执行完毕，刷新表格");
                    //等待文件扫描任务
                    // TODO
                    //刷新表格：将扫描出来的子文件，子文件都展示到文件里
                    freshTable();
                }catch(InterruptedException e){
                    e.printStackTrace();
                    scanner.shutdown();
                }
            }
        });
        task.start();
=======
>>>>>>> c9db452291d2eb63072f2c5e2f6ac1de5ebf7d73
    }

    // 刷新表格数据
    private void freshTable(){
        ObservableList<FileMeta> metas = fileTable.getItems();
        metas.clear();
<<<<<<< HEAD
        // 如果选择了某个目录，代表需要再根据搜索框的内容
        String dir=srcDirectory.getText();
        if(dir!=null&&dir.trim().length()!=0){
            String content=searchField.getText();
            //提供数据库的查询方法
            List<FileMeta> fileMetas= FileSearch.search(dir,content);
            //TODO
            //Collection--->
            //Map-->HashMap/HashTable/TreeMap
            metas.addAll(fileMetas);
        }
        //--->方法返回后，javafx表单做什么？
        //通过反射获取fileMeta类型中的属性(app.fxml文件中定义的属性)
=======
        // TODO
>>>>>>> c9db452291d2eb63072f2c5e2f6ac1de5ebf7d73
    }
}