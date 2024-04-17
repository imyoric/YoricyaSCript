package ru.yoricya.ysc.LangObjects;

public class Function {
    public String FuncName;
    public String Body;
    public Object[] Args;

    public Function(String Body){
        this.Body = Body;
    }

    public String toString(){
        return Body;
    }
}
