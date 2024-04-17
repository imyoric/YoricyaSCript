package ru.yoricya.ysc.YscFiles;

import ru.yoricya.ysc.LangObjects.Function;
import ru.yoricya.ysc.Modules.Modules;

public class YscModule extends DefaultFile{
    public final String ClassPath;
    public YscModule(String classpath){
        ClassPath = classpath;
    }

    public void addModule(){
        Modules.YscModule m = new Modules.YscModule(ClassPath);

        for (String f : getVarsKeySet()) {
            Object obj = getVar(f);
            if(obj instanceof Function) m.addFunc(((Function)obj).FuncName, (Function) obj);
        }

        Modules.addModule(m);
    }
}
