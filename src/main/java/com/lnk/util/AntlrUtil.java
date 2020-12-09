package com.lnk.util;

import com.lnk.bean.FuncInfo;
import com.lnk.bean.FileInfo;
import com.lnk.error_recovers.*;
import com.lnk.listener.*;
import com.lnk.cppparser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Antlr工具类
 */
@SuppressWarnings("Duplicates")
public class AntlrUtil {

    public static String printSyntaxTree(Parser parser, ParseTree root) {
        StringBuilder buf = new StringBuilder();
        recursive(root, buf, 0, Arrays.asList(parser.getRuleNames()));
        return buf.toString();
    }

    private static void recursive(ParseTree aRoot, StringBuilder buf, int offset, List<String> ruleNames) {
        for (int i = 0; i < offset; i++) {
            buf.append("  ");
        }
        buf.append(Trees.getNodeText(aRoot, ruleNames)).append("\n");
        if (aRoot instanceof ParserRuleContext) {
            ParserRuleContext prc = (ParserRuleContext) aRoot;
            if (prc.children != null) {
                for (ParseTree child : prc.children) {
                    recursive(child, buf, offset + 1, ruleNames);
                }
            }
        }
    }

    /**
     * @param filePath      文件路径
     * @param fileInfo   项目根目录和输出文件
     */
    public static void processFile(String filePath, FileInfo fileInfo) {
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
            ParseTreeListener parseTreeListener =new GetFuncStartEndListener();
            walker.walk(parseTreeListener, parseTree);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}