package com.lnk.listener;

import com.lnk.Main;
import com.lnk.bean.FuncInfo;
import com.lnk.cparser.*;

import java.io.BufferedWriter;
import java.io.FileWriter;

@SuppressWarnings("Duplicates")
public class CListener extends CBaseListener {
    private FuncInfo funcInfo;
    private String outputPath;

    public CListener(FuncInfo funcInfo, String outputPath){
        this.funcInfo = funcInfo;
        this.outputPath = outputPath;
    }

    @Override
    public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
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
