package ru.yoricya.ysc.Modules;

import ru.yoricya.ysc.Classes.Classes;
import ru.yoricya.ysc.LangObjects.NativeFunction;
import ru.yoricya.ysc.Ysc;

import java.util.Base64;

public class LangBase64 extends Modules.NativeModule {
    public LangBase64() {
        super("Base64");
        addFunc("decode", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped! -> func(stringOfBase64)");
                return Base64.getDecoder().decode(String.valueOf(args[0]));
            }
        });

        addFunc("encode", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped! -> func(string)");
                return Base64.getEncoder().encode(String.valueOf(args[0]).getBytes());
            }
        });

    }

    public static class EmptyElement{

        @Override
        public String toString() {
            return null;
        }
    }
}
