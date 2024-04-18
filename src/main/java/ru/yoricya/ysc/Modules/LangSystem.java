package ru.yoricya.ysc.Modules;

import ru.yoricya.ysc.LangObjects.NativeFunction;
import ru.yoricya.ysc.Ysc;

import static ru.yoricya.ysc.Ysc.YSC_VERSION;
import static ru.yoricya.ysc.Ysc.YSC_VERSION_CODE;

public class LangSystem extends Modules.NativeModule {

    final long initMillis;
    public LangSystem() {
        super("System");

        initMillis = System.currentTimeMillis();

        addFunc("currentTime", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                return System.currentTimeMillis();
            }
        });

        addFunc("initTime", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                return initMillis;
            }
        });

        addFunc("getEnv", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                return System.getenv(String.valueOf(args[0]));
            }
        });

        addFunc("yscVersion", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                return YSC_VERSION;
            }
        });

        addFunc("yscVersionCode", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                return YSC_VERSION_CODE;
            }
        });

        addFunc("lineSeparator", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                return System.lineSeparator();
            }
        });

        addFunc("exit", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                if(!(args[0] instanceof Number))
                    throw new RuntimeException("Exit Code must be a number!");

                System.exit(((Number) args[0]).intValue());
                return null;
            }
        });
    }
}
