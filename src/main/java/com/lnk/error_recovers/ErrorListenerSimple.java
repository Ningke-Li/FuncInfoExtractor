package com.lnk.error_recovers;

import com.lnk.cppparser.CPP14Parser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;

public class ErrorListenerSimple extends DefaultErrorStrategy {

    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        super.recover(recognizer, e);
        CommonTokenStream tokens = (CommonTokenStream) recognizer.getTokenStream();
        // verify current token is not EOF
        if (tokens.LA(1) != CPP14Parser.EOF)
        {// move to next token
            tokens.consume();
        }
    }
}



