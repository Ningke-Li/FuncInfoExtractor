package com.lnk.util;

import com.lnk.listener.*;
import com.lnk.bean.*;
import com.lnk.error_recovers.*;
import com.lnk.cppparser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
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

    private GetFuncStartEndListener preGenerate(String path) throws IOException {
        CharStream input = CharStreams.fromFileName(path);
        Lexer lexer = new CPP14Lexer(input);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        Parser parser = new CPP14Parser(commonTokenStream);

        ParseTree parseTree = ((CPP14Parser) parser).translationUnit();
        TestErrorListener testErrorListener=new TestErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(testErrorListener);

        GetFuncStartEndListener getFuncStartEndListener
                = new GetFuncStartEndListener();// 只需要得到函数的tree

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(getFuncStartEndListener, parseTree);

        return getFuncStartEndListener;
    }

    private void processFile(String path, Boolean flags,String testcaseID) throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException {
        GetFuncStartEndListener preProcessListener = preGenerate(path);
        ArrayList<FuncInfo> funcBeans = preProcessListener.getFuncInfos();
        CharStream input = CharStreams.fromFileName(path);
        Lexer lexer = new CPP14Lexer(input);
        List<Token> list = (List<Token>) lexer.getAllTokens();
        StringBuilder buf=new StringBuilder();
        if(flags){
            buf.append("1 ");
        }else buf.append("0 ");
        for(FuncInfo funcInfo1 : funcBeans){
            for (Token token : list){
                if(token.getLine()>= funcInfo1.getStartLine()&& token.getLine()<= funcInfo1.getEndLine()&&token.getType()!=9){
                    buf.append(token.getText());
                    buf.append(" ");
                }
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
    public static void main(String[] args) throws Exception {
        String CWEID = "20";
        String basePath = "D:\\0x00\\sard_crawler\\dataset\\svf-related\\cwe\\CWE"+CWEID+"\\source-code\\";
        String outputPath = "D:\\0x00\\sard_crawler\\dataset\\svf-related\\token-raw\\CWE"+CWEID+"\\CWE"+CWEID+".txt";

        new TokenGenerator(basePath, outputPath).start();
    }
}