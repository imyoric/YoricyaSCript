package ru.yoricya.ysc.Modules;


import ru.yoricya.ysc.LangObjects.NativeFunction;
import ru.yoricya.ysc.Ysc;

import java.util.Random;

public class LangMath extends Modules.NativeModule {
    Random random = new Random();
    public LangMath() {
        super("Math");

        addFunc("abs", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                if(args[0] instanceof Double || args[0] instanceof Float)
                    return Math.abs(((Number)args[0]).doubleValue());
                else
                    return Math.abs(((Number)args[0]).longValue());
            }
        });

        addFunc("max", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 2)
                    throw new RuntimeException("Arguments cant be skipped!");
                if(args[0] instanceof Double || args[0] instanceof Float)
                    return Math.max(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue());
                else
                    return Math.max(((Number)args[0]).longValue(), ((Number)args[1]).longValue());
            }
        });

        addFunc("minus", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 2)
                    throw new RuntimeException("Arguments cant be skipped!");
                if(args[0] instanceof Double || args[0] instanceof Float)
                    return ((Number)args[0]).doubleValue() - ((Number)args[1]).doubleValue();
                else
                    return ((Number)args[0]).longValue() - ((Number)args[1]).longValue();
            }
        });

        addFunc("plus", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 2)
                    throw new RuntimeException("Arguments cant be skipped!");
                if(args[0] instanceof Double || args[0] instanceof Float)
                    return ((Number)args[0]).doubleValue() + ((Number)args[1]).doubleValue();
                else
                    return ((Number)args[0]).longValue() + ((Number)args[1]).longValue();
            }
        });

        addFunc("rand", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 2)
                    throw new RuntimeException("Arguments cant be skipped!");

                return random.nextInt(((Number)args[1]).intValue() + 1 - ((Number)args[0]).intValue()) + ((Number)args[0]).intValue();
            }
        });

        addFunc("bindNewRandom", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length == 1 && args[0] instanceof Number)
                    random = new Random(((Number)args[0]).longValue());
                else
                    random = new Random();

                return true;
            }
        });

        addFunc("min", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 2)
                    throw new RuntimeException("Arguments cant be skipped!");
                if(args[0] instanceof Double || args[0] instanceof Float)
                    return Math.min(((Number)args[0]).doubleValue(), ((Number)args[1]).doubleValue());
                else
                    return Math.min(((Number)args[0]).longValue(), ((Number)args[1]).longValue());
            }
        });

        addFunc("acos", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                return Math.acos(((Number)args[0]).doubleValue());
            }
        });

        addFunc("addExact", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 2)
                    throw new RuntimeException("Arguments cant be skipped!");

                return Math.addExact(((Number)args[0]).longValue(), ((Number)args[1]).longValue());
            }
        });

        addFunc("asin", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                return Math.asin(((Number)args[0]).doubleValue());
            }
        });

        addFunc("atan", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                return Math.atan(((Number)args[0]).doubleValue());
            }
        });

        addFunc("acos", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                return Math.acos(((Number)args[0]).doubleValue());
            }
        });

        addFunc("incrementExact", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                return Math.incrementExact(((Number)args[0]).longValue());
            }
        });

        addFunc("decrementExact", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                return Math.decrementExact(((Number)args[0]).longValue());
            }
        });

        addFunc("exp", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                return Math.exp(((Number)args[0]).doubleValue());
            }
        });

        addFunc("floor", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                return Math.floor(((Number)args[0]).doubleValue());
            }
        });

    }
}
