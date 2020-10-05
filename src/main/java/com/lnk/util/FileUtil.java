package com.lnk.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author wujun
 * 文件工具类
 */
public class FileUtil {

    /**
     * 递归列出目录下所有目标后缀的文件
     * @param dir   目标文件夹
     * @param exts  目标文件后缀
     * @return
     */
    public static List<String> listAllFiles(String dir, String[] exts){
        List<String> filesPath = new ArrayList<>();
        File root = new File(dir);
        if (!root.exists()){
            return filesPath;
        }
        File[] subFiles = root.listFiles();
        if (subFiles == null){
            return filesPath;
        }
        for (File f: subFiles){
            if (f.isDirectory()){
                filesPath.addAll(listAllFiles(f.getAbsolutePath(), exts));
            }else{
                for (String ext: exts){
                    if (f.getName().endsWith(ext)){
                        filesPath.add(f.getAbsolutePath());
                        break;
                    }
                }
            }
        }
        return filesPath;
    }
}

