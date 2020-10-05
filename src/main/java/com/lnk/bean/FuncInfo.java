package com.lnk.bean;

public class FuncInfo {

    private String path;
    private String file;
    private String funcDefName;
    private int startLine;
    private int endLine;

    public FuncInfo() {
    }

    public FuncInfo(String path, String file, String funcDefName, int startLine, int endLine) {
        this.path = path;
        this.file = file;
        this.funcDefName = funcDefName;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFuncDefName() {
        return funcDefName;
    }

    public void setFuncDefName(String funcDefName) {
        this.funcDefName = funcDefName;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }


    @Override
    public String toString() {
        String res = "";
        res += path + ",";
        res += file + ",";
        res += funcDefName + ",";
        res += startLine + ",";
        res += endLine + ",";
        return res;
    }
}

