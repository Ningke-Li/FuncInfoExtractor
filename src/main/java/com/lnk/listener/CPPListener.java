package com.lnk.listener;

import com.lnk.Main;
import com.lnk.bean.FuncInfo;
import com.lnk.cppparser.CPP14Parser;
import com.lnk.cppparser.CPP14ParserBaseListener;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * CPP监听器
 */
@SuppressWarnings("Duplicates")
public class CPPListener extends CPP14ParserBaseListener {

    private FuncInfo funcInfo;
    private String outputPath;
    private ParseTree currentTopTree = null; //当前顶层函数的语法树
    private String currentTopName = null; //当前顶层函数的名称

    public CPPListener(FuncInfo funcInfo, String outputPath){
        this.funcInfo = funcInfo;
        this.outputPath = outputPath;
    }

    @Override
    public void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
        try {
            funcInfo.setFuncDefName(ctx.declarator().start.getText());
            funcInfo.setStartLine(ctx.start.getLine());
            funcInfo.setEndLine(ctx.stop.getLine());
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

