package ru.yoricya.ysc.Modules;

import ru.yoricya.ysc.LangObjects.NativeFunction;
import ru.yoricya.ysc.Ysc;
import ru.yoricya.ysc.YscParseUtils;

import java.util.concurrent.ForkJoinPool;

public class LangThread extends Modules.NativeModule {
    ForkJoinPool ThreadPool = new ForkJoinPool();

    public LangThread() {
        super("Thread");

        addFunc("runAsThreadPool", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length < 1)
                    throw new RuntimeException("Give me a function body as argument!");

                Object[] dest = new Object[0];

                if(args.length-1 > 0) {
                    dest = new Object[args.length - 1];
                    System.arraycopy(args, 1, dest, 0, dest.length);
                }

                Object[] finalDest = dest;
                ThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            YscParseUtils.parseFunc(ysc, args[0].toString(), finalDest);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                return true;
            }
        });

        addFunc("initThreadPool", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length > 0 && args[0] instanceof Number) {
                    int t = ((Number)args[0]).intValue();
                    if(t < 0)
                        throw new RuntimeException("Threads Count cant be < 0!");
                    ThreadPool = new ForkJoinPool(t);
                }

                ThreadPool = new ForkJoinPool();
                return true;
            }
        });

        addFunc("runAsThread", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length < 1)
                    throw new RuntimeException("Give me a function body as argument!");

                Object[] dest = new Object[0];

                if(args.length-1 > 0) {
                    dest = new Object[args.length - 1];
                    System.arraycopy(args, 1, dest, 0, dest.length);
                }

                Object[] finalDest = dest;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            YscParseUtils.parseFunc(ysc, args[0].toString(), finalDest);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

                return true;
            }
        });
    }
}
