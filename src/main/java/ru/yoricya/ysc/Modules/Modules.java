package ru.yoricya.ysc.Modules;

import ru.yoricya.ysc.LangObjects.Function;
import ru.yoricya.ysc.LangObjects.NativeFunction;
import ru.yoricya.ysc.Ysc;

import java.util.HashMap;
import java.util.Set;

public class Modules {
    public static final HashMap<String, Module> Modules = new HashMap<>();
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
        public Function[] getAllFunctions() {
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
        public Function[] getAllFunctions() {
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
        public abstract Function[] getAllFunctions();
        public final Function[] getAllFuncs(){
            Function[] fns = getAllFunctions();
            Function[] newFns = new Function[fns.length+1];

            System.arraycopy(fns, 0, newFns, 0, fns.length);

            Function serialize = new NativeFunction() {
                @Override
                public Object run(Ysc ysc, Object[] args) {
                    return serialize();
                }
            }.setName("serialize");

            newFns[fns.length] = serialize;
            return newFns;
        }
        public String serialize(){
            String space = "module "+classPath+";\n\n";

            Function[] Functions = getAllFuncs();

            for(Function fn: Functions){
                if(fn == null) continue;
                if(fn.NeededArgs != null){
                    space += "\n";
                    space += "@Args -> ";
                    if(fn.NeededArgs.isEmpty()){
                        space += "\"None args\"";
                    }else for(String ks: fn.NeededArgs.keySet()){
                        String comment = fn.NeededArgs.get(ks);
                        space += ks+": "+comment+"\",";
                    }

                    space += ";";
                }

                if(fn instanceof NativeFunction){
                    space += "\nnative func "+fn.FuncName;
                    space += "{}\n";
                }else if(fn.Body != null){
                    space += "\nfunc "+fn.FuncName;
                    space += fn.Body+"\n";
                }else{
                    space += "\nnobody func "+fn.FuncName;
                    space += "{}\n";
                }
            }

            return space;
        }
        @Override
        public String toString() {
            return classPath;
        }
    }
}
