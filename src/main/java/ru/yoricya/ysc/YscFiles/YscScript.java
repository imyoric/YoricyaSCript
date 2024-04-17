package ru.yoricya.ysc.YscFiles;

import ru.yoricya.ysc.LinkVariableMap;
import ru.yoricya.ysc.Ysc;

public class YscScript extends DefaultFile{
    public YscScript(Ysc ysc){
        this.reInitObjects(new LinkVariableMap(ysc.getVariableMap()));
    }
}
