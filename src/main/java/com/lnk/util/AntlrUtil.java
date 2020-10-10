package com.lnk.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lnk.bean.FuncInfo;
import com.lnk.bean.FileInfo;
import com.lnk.error_recovers.ErrorListenerSimple;
import com.lnk.listener.*;
import com.lnk.cppparser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
import java.util.ArrayList;

/**
 * Antlr工具类
 */
@SuppressWarnings("Duplicates")
public class AntlrUtil {
    /**
     * @param filePath      文件路径
     * @param fileInfo   项目根目录和输出文件
     */
    public static void processFile(String filePath, FileInfo fileInfo){
        try {
            Lexer lexer = new CPP14Lexer(CharStreams.fromFileName(filePath));
            CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
            Parser parser = new CPP14Parser(commonTokenStream);
            ParseTree parseTree = null;
            parseTree = ((CPP14Parser)parser).translationUnit();
            ParseTreeWalker walker = new ParseTreeWalker();
            FuncInfo funcInfo = new FuncInfo();
            funcInfo.setPath(filePath);
            String os = System.getProperty("os.name");
             if (os.toLowerCase().contains("windows")){
                funcInfo.setFile(filePath.substring(filePath.lastIndexOf("\\") + 1));
            }else{
                funcInfo.setFile(filePath.substring(filePath.lastIndexOf("/") + 1));
            }
            ParseTreeListener parseTreeListener =new CPPListener(funcInfo, fileInfo.getOutputPath());
            walker.walk(parseTreeListener, parseTree);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

