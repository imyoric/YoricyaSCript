package ru.yoricya.ysc.LangObjects;

import ru.yoricya.ysc.Ysc;
import java.util.HashMap;

public abstract class NativeFunction extends Function {
    public NativeFunction() {
        super(null);
    }

    public NativeFunction(String name) {
        super(null);
        this.FuncName = name;
    }

    public abstract Object run(Ysc ysc, Object[] args);
    @Override
    public String toString() {
        throw new RuntimeException("NativeFunction not contain Body Script!");
    }

    public NativeFunction addNeedArg(String arg, String why){
        if(NeededArgs == null) NeededArgs = new HashMap<>();
        NeededArgs.put(arg, why);
        return this;
    }

    public NativeFunction setName(String funcName){
        FuncName = funcName;
        return this;
    }
}
