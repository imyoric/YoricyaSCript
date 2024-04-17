package ru.yoricya.ysc.LangObjects;


import ru.yoricya.ysc.Ysc;

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
}
