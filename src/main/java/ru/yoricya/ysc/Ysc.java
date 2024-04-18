package ru.yoricya.ysc;

import ru.yoricya.ysc.Classes.Classes;
import ru.yoricya.ysc.LangObjects.Function;
import ru.yoricya.ysc.LangObjects.NativeFunction;
import ru.yoricya.ysc.LangObjects.ReturnObject;
import ru.yoricya.ysc.Modules.LangFileUtils;
import ru.yoricya.ysc.YscFiles.DefaultFile;
import ru.yoricya.ysc.YscFiles.YscClass;
import ru.yoricya.ysc.YscFiles.YscModule;
import ru.yoricya.ysc.YscFiles.YscScript;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Ysc {
    public final static String YSC_VERSION = "1.0";
    public final static Long YSC_VERSION_CODE = 1L;
    private LinkVariableMap objects = new LinkVariableMap();

    public void putVar(String name, Object var, boolean isNewLink){
        if(isNewLink)
            this.objects.putWithNewLink(name, var);
        else
            this.objects.put(name, var);
    }

    public void putVar(String name, Object var){
        this.objects.put(name, var);
    }

    public Set<String> getVarsKeySet(){
        return this.objects.keyset();
    }

    public void reInitObjects(LinkVariableMap variableMap){
        objects = variableMap;
    }

    public LinkVariableMap getVariableMap(){
        return objects;
    }

    public Object getVar(String name){
        return this.objects.get(name);
    }

    public Ysc(){
        objects.put("print", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length < 1) return false;
                System.out.print(args[0]);
                return true;
            }
        });

        objects.put("println", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length < 1) return false;
                System.out.println(args[0]);
                return true;
            }
        });

        objects.put("printf", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length < 1) return false;
                if(!(args[0] instanceof String)) return false;
                Object[] dest = new Object[args.length-1];
                System.arraycopy(args, 1, dest, 0, args.length-1);
                System.out.printf((String) args[0], dest);
                return true;
            }
        });

        objects.put("new", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length < 1)
                    throw new RuntimeException("Class Name Invalid!");

                Classes.Class clas = Classes.getDuplicateClass(String.valueOf(args[0]));

                if(clas == null)
                    throw new RuntimeException("Class '"+args[0]+"' - Not found!");

                return clas;
            }
        });
    }

    public int parse(String script){
        DefaultFile file = YscParseUtils.ParseFunctionalSpace(this, script);
        if(file instanceof YscModule) {
            ((YscModule) file).addModule();
            return 0;
        }

        if(file instanceof YscClass) {
            ((YscClass) file).addClass();
            return 0;
        }

        if(objects.get("main") != null && objects.get("main") instanceof Function) {
            ReturnObject returnObject = YscParseUtils.parseFunc(this, ((Function) objects.get("main")).Body, new String[]{"12", "34"});
            if(returnObject == null) return 0;
            if(returnObject.get() instanceof Integer) return (int) returnObject.get();
        }

        return 0;
    }

    public DefaultFile parse1(String script){
        return YscParseUtils.ParseFunctionalSpace(this, script);
    }


    public Ysc loadProjectDir(String pathToDir){
        File f = new File(pathToDir);
        File[] yscFiles = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String[] s = pathname.getName().split("\\.");
                return s[s.length-1].equals("ysc");
            }
        });

        List<String> mainScripts = new ArrayList<>();
        if(yscFiles != null) for(File file: yscFiles) {
            if(file.isDirectory()){
                loadProjectDir(file.getAbsolutePath());
                continue;
            }

            String script = LangFileUtils.readFile(file.getAbsolutePath());
            if (script == null) continue;

            short fileType = YscParseUtils.getFileType(script);
            if(fileType == DefaultFile.SCRIPT_FILE_TYPE){
                mainScripts.add(script);
                continue;
            }

            Ysc y = new Ysc();
            DefaultFile deffile = y.parse1(script);

            if(deffile instanceof YscClass)
                ((YscClass) deffile).addClass();

            if(deffile instanceof YscModule)
                ((YscModule) deffile).addModule();
        }

        for(String s: mainScripts){
            parse1(s);
        }

        return this;
    }

    public Object runMain(Object[] args){
        if(objects.get("main") != null && objects.get("main") instanceof Function) {
            ReturnObject returnObject = YscParseUtils.parseFunc(this, ((Function) objects.get("main")).Body, args);
            if(returnObject == null) return 0;
            if(returnObject.get() instanceof Integer) return returnObject.get();
        }else
            throw new RuntimeException("Main Function Not Found!");

        return null;
    }
}
