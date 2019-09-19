package ro.dobrescuandrei.autoextendstestdesktop;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import ro.dobrescuandrei.autoextends.processor.AutoExtendsProcessor;

public class Main
{
    public static void main(String[] args)
    {
        List<String> arguments=new LinkedList<>();
        arguments.add("-processor");
        arguments.add(AutoExtendsProcessor.class.getName());

        List<File> annotationsModuleSourceFiles=getSourceFilesInDir(new File("annotations"));
        for (File file : annotationsModuleSourceFiles)
            arguments.add(file.getAbsolutePath());

        List<File> testModuleSourceFiles=getSourceFilesInDir(new File("test"));
        for (File file : testModuleSourceFiles)
            arguments.add(file.getAbsolutePath());

        JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, arguments.toArray(new String[0]));
    }

    public static List<File> getSourceFilesInDir(File dir)
    {
        List<File> sourceFiles=new LinkedList<>();

        if (dir.isDirectory())
        {
            File[] files=dir.listFiles();
            if (files!=null)
            {
                for (File file : files)
                {
                    if (file.isDirectory())
                    {
                        sourceFiles.addAll(getSourceFilesInDir(file));
                    }
                    else
                    {
                        if (file.getName().endsWith(".java"))
                            sourceFiles.add(file);
                    }
                }
            }
        }

        return sourceFiles;
    }
}
