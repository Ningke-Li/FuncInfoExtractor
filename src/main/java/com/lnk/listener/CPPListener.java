package com.lnk.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lnk.Main;
import com.lnk.bean.FuncInfo;
import com.lnk.cppparser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.Trees;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * CPP监听器
 */
@SuppressWarnings("Duplicates")
public class CPPListener extends CPP14ParserBaseListener {
    private FuncInfo funcInfo;
    private String outputPath;
    private ArrayList<FuncInfo> funcInfos = new ArrayList<FuncInfo>();// 用户自定义函数列表
    private ParseTree currentTopTree = null; //当前顶层函数的语法树
    private String currentTopName = null; //当前顶层函数的名称
    ArrayList<Integer> delLines = new ArrayList<>();
    ArrayList<Integer> addLines = new ArrayList<>();

    public CPPListener(FuncInfo funcInfo, String outputPath){
        this.funcInfo = funcInfo;
        this.outputPath = outputPath;
    }

    @Override
    public void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
        try {
            this.currentTopTree = ctx;
            ParseTreeWalker walker = new ParseTreeWalker();
            GetFuncDefNameListener getFuncDefNameListener = new GetFuncDefNameListener();
            walker.walk(getFuncDefNameListener, ctx);
            this.currentTopName = getFuncDefNameListener.getFuncDefName();

            funcInfo.setStartLine(ctx.start.getLine());
            funcInfo.setEndLine(ctx.stop.getLine());
            funcInfo.setFuncDefName(this.currentTopName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, true));
            writer.write(funcInfo.toString());
            writer.newLine();
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //读取Json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean getJsonInfo(int startLines,int endLines) {
        String path = "D:\\IdeaProjects\\cpptest\\src\\main\\resources\\test.json";
        String s = readJsonFile(path);
        JSONObject jobj = JSON.parseObject(s);
        JSONArray commitInfo = jobj.getJSONArray("file_modify");//构建JSONArray数组
        for (int i = 0; i < commitInfo.size(); i++) {
            JSONObject key = (JSONObject) commitInfo.get(i);
            JSONArray del_lines = key.getJSONArray("del_lines");
            JSONArray add_lines = key.getJSONArray("add_lines");
            for (int j = 0; j < del_lines.size(); j++) {
                Integer del = (Integer) del_lines.get(j);
                this.delLines.add(del);
            }
            for (int k = 0; k < add_lines.size(); k++) {
                Integer add = (Integer) add_lines.get(k);
                if (add > startLines && add < endLines)
                    return true;
            }
        }
        return false;
    }
}


