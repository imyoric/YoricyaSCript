package ru.yoricya.ysc.Modules;

import ru.yoricya.ysc.LangObjects.NativeFunction;
import ru.yoricya.ysc.Ysc;

import java.nio.charset.StandardCharsets;

public class LangStringUtils extends Modules.NativeModule {

    public LangStringUtils() {
        super("StringUtils");

        addFunc("replace", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 3)
                    throw new RuntimeException("Arguments cant be skipped! -> func(string, search, replaceTo)");


                return args[0].toString().replaceAll(args[1].toString(), args[2].toString());
            }
        });

        addFunc("equals", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 2)
                    throw new RuntimeException("Arguments cant be skipped! -> func(string1, string2)");

                return args[0].toString().equals(args[1].toString());
            }
        });

        addFunc("toLowerCase", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped! -> func(string)");

                return args[0].toString().toLowerCase();
            }
        });

        addFunc("toUpperCase", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped! -> func(string)");

                return args[0].toString().toUpperCase();
            }
        });

        addFunc("getBytes", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped! -> func(string)");

                return args[0].toString().getBytes(StandardCharsets.UTF_8);
            }
        });
    }
}
