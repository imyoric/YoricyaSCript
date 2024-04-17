package ru.yoricya.ysc.YscFiles;

import ru.yoricya.ysc.Classes.Classes;
import ru.yoricya.ysc.LangObjects.Function;

public class YscClass extends DefaultFile{
    public final String ClassPath;
    public YscClass(String classpath){
        ClassPath = classpath;
    }

    public void addClass(){
        Classes.Class m = new Classes.Class(ClassPath);

        for (String f : getVarsKeySet()) {
            Object obj = getVar(f);
            m.putVar(f, obj);
        }

        Classes.addClass(m);
    }
}
