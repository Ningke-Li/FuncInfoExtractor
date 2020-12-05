package com.lnk.listener;

import com.lnk.bean.FuncInfo;
import com.lnk.cppparser.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
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
}