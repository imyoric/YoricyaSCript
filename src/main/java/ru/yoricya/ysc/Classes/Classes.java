package ru.yoricya.ysc.Classes;

import ru.yoricya.ysc.LangObjects.Function;
import ru.yoricya.ysc.LangObjects.NativeFunction;
import ru.yoricya.ysc.LinkVariableMap;
import ru.yoricya.ysc.Ysc;

import java.util.Arrays;
import java.util.HashMap;

public class Classes {
    private static final HashMap<String, Class> Classes = new HashMap<>();

    public static Class getOriginalClass(String name){
        return Classes.get(name);
    }

    public static Class getDuplicateClass(String name){
        return new Class(name).duplicate(getOriginalClass(name));
    }

    public static void addClass(Class cls){
        Classes.put(cls.ClassName, cls);
    }

    public static class Class extends Ysc{
        public final String ClassName;

        public Class(String name){
            ClassName = name;
        }

        public void addFunction(String name, Function func){
            this.putVar(name, func);
        }

        public Class duplicate(Class other){
            if(other == null) return null;
            for(String key: other.getVarsKeySet()){
                this.putVar(key, other.getVar(key));
            }

            putVar("serialize", new NativeFunction() {
                @Override
                public Object run(Ysc ysc, Object[] args) {
                    return serialize();
                }
            });

            return this;
        }

        public String serialize(){
            String space = "class "+ClassName+";\n\n";

            for(String key: this.getVarsKeySet()){
                Object get = this.getVar(key);

                if(get instanceof Function){
                    Function fn = (Function) get;
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
                        space += "\nnative func "+key;
                        space += "{}\n";
                    }else if(fn.Body != null){
                        space += "\nfunc "+key;
                        space += fn.Body+"\n";
                    }else{
                        space += "\nnobody func "+key;
                        space += "{}\n";
                    }
                }else{
                    space += "\nvar "+key+" \""+get+"\" //WARN, unknown type\n";
                }
            }

            return space;
        }

        @Override
        public String toString() {
            return getVariableMap().LocalMapByName.toString();
        }
    }

    public static class Array extends Class{
        public final Object[] Array;

        public Array(int len){
            super("Array");
            Array = new Object[len];
        }

        public Array(Object[] arr){
            super("Array");
            Array = arr;
        }

        @Override
        public void putVar(String name, Object var) {
            int i;

            try{
                i = Integer.parseInt(name);
            }catch (Exception e){
                throw new RuntimeException("Array cant be put value by string key! ("+name+")");
            }

            Array[i] = var;
        }

        @Override
        public Object getVar(String name) {
            int i;

            if(name.equals("length"))
                return Array.length;

            if(name.equals("this.args"))
                return Arrays.toString(Array);

            try{
                i = Integer.parseInt(name);
            }catch (Exception e){
                throw new RuntimeException("Array cant be get value by string key! ("+name+")");
            }

            return Array[i];
        }

        @Override
        public String toString() {
            return "Array";
        }
    }

//    public static class FileClass extends Ysc{
//        public final String ClassName;
//
//        public FileClass(Ysc parsedFile){
//            ClassName = "";
//            this.objects = new LinkVariableMap(parsedFile.objects);
//        }
//
//        public void addFunction(Function func){
//            this.objects.put(func.FuncName, func);
//        }
//    }
}
