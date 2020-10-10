package com.lnk.listener;

import com.lnk.cppparser.CPP14Parser;
import com.lnk.cppparser.CPP14ParserBaseListener;

import java.util.ArrayList;

public class GetFuncDefParamsListener extends CPP14ParserBaseListener {
    private ArrayList<String> paramList = new ArrayList<>();

    @Override
    public void enterUnqualifiedId(CPP14Parser.UnqualifiedIdContext ctx) {
        this.paramList.add(ctx.getText());
    }

    public ArrayList<String> getParamList() {
        return paramList;
    }
}
