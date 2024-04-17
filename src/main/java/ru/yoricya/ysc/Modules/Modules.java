package ru.yoricya.ysc.Modules;

import ru.yoricya.ysc.LangObjects.Function;
import ru.yoricya.ysc.LangObjects.NativeFunction;

import java.util.HashMap;
import java.util.Set;

public class Modules {
    private static final HashMap<String, Module> Modules = new HashMap<>();
    private final static byte b = init();
    private static byte init(){
        ru.yoricya.ysc.Modules.Modules.addModule(new LangMath());
        ru.yoricya.ysc.Modules.Modules.addModule(new LangSystem());
        ru.yoricya.ysc.Modules.Modules.addModule(new LangThread());
        ru.yoricya.ysc.Modules.Modules.addModule(new LangFileUtils());
        ru.yoricya.ysc.Modules.Modules.addModule(new LangStringUtils());
        ru.yoricya.ysc.Modules.Modules.addModule(new LangArrays());
        ru.yoricya.ysc.Modules.Modules.addModule(new LangBase64());
        return 0;
    }

    public static void addModule(Module module){
        Modules.put(module.classPath, module);
    }

    public static Module get(String classpath){
        return Modules.get(classpath);
    }

    public static class NativeModule extends Module{
        public HashMap<String, NativeFunction> Functions = new HashMap<>();
        public NativeModule(String classpath) {
            super(classpath);
        }

        @Override
        public Function[] getAllFuncs() {
            Set<String> keyset = Functions.keySet();
            Function[] functions = new Function[keyset.size()];

            int i = 0;
            for(String s: keyset){
                functions[i] = Functions.get(s);
                functions[i].FuncName = s;
                i++;
            }

            return functions;
        }

        public void addFunc(String name, NativeFunction func){
            Functions.put(name, func);
        }
    }

    public static class YscModule extends Module{
        public HashMap<String, Function> Functions = new HashMap<>();
        public YscModule(String classpath) {
            super(classpath);
        }
        @Override
        public Function[] getAllFuncs() {
            Set<String> keyset = Functions.keySet();
            Function[] functions = new Function[keyset.size()];

            int i = 0;
            for(String s: keyset){
                functions[i] = Functions.get(s);
                functions[i].FuncName = s;
                i++;
            }

            return functions;
        }

        public void addFunc(String name, Function func){
            Functions.put(name, func);
        }
    }

    public static abstract class Module {
        public final String classPath;

        public Module(String classpath) {
            this.classPath = classpath;
        }

        public abstract Function[] getAllFuncs();
    }
}
