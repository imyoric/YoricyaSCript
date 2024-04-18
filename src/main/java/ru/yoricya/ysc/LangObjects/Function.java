package ru.yoricya.ysc.LangObjects;


import java.util.HashMap;

public class Function {
    public String FuncName;
    public String Body;
    public Object[] Args;
    public HashMap<String, String> NeededArgs;
    public Function(String Body){
        this.Body = Body;
    }
    public String toString(){
//        NeededArgs[0][0] = "arg";
//        NeededArgs[0][1] = "Need arg";
        return Body;
    }
}
