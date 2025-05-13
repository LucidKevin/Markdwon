import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    public static String INDEX_PATH = "index.md";
    public static void main(String[] args) {
        //获取当前路径
        File currentDir = new File(".").getAbsoluteFile();

        //写入 index.md 文档的内容
        StringBuffer content = new StringBuffer("---\n" +
                "layout: default\n" +
                "title: 主页\n" +
                "---\n" +
                "\n" +
                "## Mardown 文件管理\n");


        //处理文件
        if(currentDir.isDirectory()){
            File[] files = currentDir.listFiles();
            if(files != null){
                //遍历当前父文件夹中文件夹
                for(File file : files){
                    if(file.isDirectory() && !file.getName().startsWith(".") && !file.getName().equals("out")){
                        content.append("### ").append(file.getName()).append("\n");
                        for(File childfile : file.listFiles()){
                            //遍历文件夹中mardown 文件
                            if(!childfile.getName().startsWith(".") && childfile.getName().endsWith(".md")) {
                                content.append("- [").append(childfile.getName().substring(0, childfile.getName().length()-3)).append("](").append("./").append(file.getName()).append("/").append(childfile.getName()).append(")").append("\n");
                            }
                        }
                    }
                }
            }
        }

        //写入 index.md 文件
        try(FileWriter writer = new FileWriter(INDEX_PATH)){
            writer.write(content.toString());
            System.out.println("文件写入成功");
        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
