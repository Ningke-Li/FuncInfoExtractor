package com.lnk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lnk.bean.FuncInfo;
import com.lnk.bean.FileInfo;
import com.lnk.listener.*;
import com.lnk.cparser.*;
import com.lnk.cppparser.*;
import com.lnk.listener.CListener;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.*;

/**
 * Antlr工具类
 */
@SuppressWarnings("Duplicates")
public class AntlrUtil {

    /**
     * 解析单个文件
     * @param filePath      文件路径
     * @param fileInfo   项目根目录和输出文件
     */
    public static void processFile(String filePath, FileInfo fileInfo){
        try {
            boolean isCFile = filePath.endsWith(".c");
            Lexer lexer = isCFile? new CLexer(CharStreams.fromFileName(filePath)): new CPP14Lexer(CharStreams.fromFileName(filePath));
            CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
            Parser parser = isCFile?new CParser(commonTokenStream): new CPP14Parser(commonTokenStream);
            ParseTree parseTree = null;
            if (parser instanceof CParser){
                parseTree = ((CParser)parser).compilationUnit();
            }else{
                parseTree = ((CPP14Parser)parser).translationUnit();
            }
            ParseTreeWalker walker = new ParseTreeWalker();
            FuncInfo funcInfo = new FuncInfo();
            funcInfo.setPath(filePath);
            String os = System.getProperty("os.name");
             if (os.toLowerCase().contains("windows")){
                funcInfo.setFile(filePath.substring(filePath.lastIndexOf("\\") + 1));
            }else{
                funcInfo.setFile(filePath.substring(filePath.lastIndexOf("/") + 1));
            }
            ParseTreeListener parseTreeListener = isCFile? new CListener(funcInfo, fileInfo.getOutputPath()): new CPPListener(funcInfo, fileInfo.getOutputPath());
            walker.walk(parseTreeListener, parseTree);
            List<String> ruleNamesList = Arrays.asList(parser.getRuleNames());
            String prettyTree = TreeUtils.toPrettyTree(parseTree, ruleNamesList);
            System.out.println(prettyTree);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

