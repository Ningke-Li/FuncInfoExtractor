package com.lnk.util;


import com.lnk.bean.FuncInfo;
import com.lnk.bean.FileInfo;
import com.lnk.error_recovers.*;
import com.lnk.cppparser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import com.lnk.listener.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

/**
 * Antlr工具类
 */
@SuppressWarnings("Duplicates")
public class TokenGenerator {
    private String xmlPath = null;
    private String basePath = null;
    private String outputPath = null;

    public TokenGenerator(String _basePath, String _outputPath) throws IOException {
        this.xmlPath = _basePath+"manifest.xml";
        this.outputPath = _outputPath;
        this.basePath = _basePath;
    }

    private void readFromXml() throws Exception {
        File file = new File(this.xmlPath);
        Document document = new SAXReader().read(file);
        Element root = document.getRootElement();
        List<Element> testCaseList = root.elements("testcase");
        for (Element ele : testCaseList) {
            List<Element> fileList = ele.elements("file");
            String testcaseID = ele.attributeValue("id");
            for (Element f : fileList) {
                String filePath = f.attributeValue("path");
                if(filePath.contains(".c")||filePath.contains(".cpp")||filePath.contains(".mm")||filePath.contains(".cc")){
                    Boolean flags = false;
                    List<Element> mixedList = f.elements("mixed");
                    List<Element> flawList = f.elements("flaw");
                    if (!filePath.contains("/testcases/shared/") && !filePath.contains("/testcases/app/")) {
                        for (Element line : flawList) {
                            if (line.attributeValue("line")!=null)
                                flags=true;
                        }
                        for (Element line : mixedList) {
                            if(line.attributeValue("line")!=null);
                            flags=true;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(this.basePath).append(filePath);
                        String path = stringBuilder.toString();
                        this.processFile(path,flags,testcaseID);
                    }
                }
            }
        }
        System.out.println("xml ok");
    }

    private void processFile(String path, Boolean flags,String testcaseID) throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException {
        CharStream input = CharStreams.fromFileName(path);
        Lexer lexer = new CPP14Lexer(input);
        List<Token> list = (List<Token>) lexer.getAllTokens();
        StringBuilder buf=new StringBuilder();
        if(flags){
            buf.append("1 ");
        }else buf.append("0 ");
        for(Token token : list){
            if(token.getType()==9){
            }else{
                buf.append(token.getText());
                buf.append(" ");
            }
        }
        buf.append("\n");
        FileWriter outFile=new FileWriter(outputPath,true);
        outFile.write(buf.toString());
        outFile.flush();
        outFile.close();
    }

    public void start() throws Exception {
        this.readFromXml();
    }
    /**
     *
     * str[0]:项目根目录
     * str[1]:结果文件输出路径
     */
    /*public static void main(String[] args) throws Exception {
        String CWEID = "862";
        String basePath = "D:\\0x00\\sard_crawler\\dataset\\svf-related\\cwe\\CWE"+CWEID+"\\source-code\\";
        String outputPath = "D:\\0x00\\sard_crawler\\dataset\\svf-related\\cwe\\CWE"+CWEID+"\\token-raw.txt";

        new TokenGenerator(basePath, outputPath).start();
    }
    */
}