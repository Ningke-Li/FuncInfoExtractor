package com.lnk.listener;
import com.lnk.cppparser.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;

public class GetFuncDefNameListener extends CPP14ParserBaseListener {
  private String funcDefName = null;
  private boolean isFirstTriggered = true;
  private ArrayList<String> paramList = null;

  @Override
  public void enterUnqualifiedId(CPP14Parser.UnqualifiedIdContext ctx) {
    if(this.isFirstTriggered){
      this.funcDefName = ctx.getText();
    }
    this.isFirstTriggered = false;
  }

  @Override
  public void enterParameterDeclarationList(CPP14Parser.ParameterDeclarationListContext ctx) {
    ParseTree funcCallTree = ctx;
    ParseTreeWalker walker = new ParseTreeWalker();
    GetFuncDefParamsListener getFuncDefParams = new GetFuncDefParamsListener();
    walker.walk(getFuncDefParams,ctx.parent);
  }
  public String getFuncDefName(){
    return this.funcDefName;
  }
}