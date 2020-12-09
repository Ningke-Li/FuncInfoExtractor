package com.lnk.listener;

import com.lnk.bean.*;
import com.lnk.listener.*;
import com.lnk.cppparser.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetFuncStartEndListener extends CPP14ParserBaseListener{
    private ArrayList<FuncInfo> funcInfos = new ArrayList<FuncInfo>();// 用户自定义函数列表
    private ParseTree currentTopTree = null; //当前顶层函数的语法树
    private String currentTopName = null; //当前顶层函数的名称
    private Map<String, ArrayList<String>> paramMap = new HashMap<>(); // key为函数定义时的名字，value为函数定义（非调用）的参数列表
    private ArrayList<String> currentTopParamList = null;

    @Override
    public void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
        this.currentTopTree = ctx;
        ParseTreeWalker walker = new ParseTreeWalker();
        GetFuncDefNameListener getFuncDefNameListener = new GetFuncDefNameListener();
        walker.walk(getFuncDefNameListener, ctx);
        this.currentTopName = getFuncDefNameListener.getFuncDefName();
        this.currentTopParamList = getFuncDefNameListener.getParamList();
        this.paramMap.put(this.currentTopName, this.currentTopParamList);

        // 用户自定义函数，用于函数内控制流分析
        FuncInfo funcInfo = new FuncInfo();
        funcInfo.setStartLine(ctx.start.getLine());
        funcInfo.setEndLine(ctx.stop.getLine());
        funcInfo.setFuncDefName(this.currentTopName);
        funcInfo.setParamList(this.currentTopParamList);
        funcInfo.setFuncInfoTree(ctx);
        this.funcInfos.add(funcInfo);

    }

    public ArrayList<FuncInfo> getFuncInfos() {
        return funcInfos;
    }
}
