package ru.yoricya.ysc.LangObjects;

public class ReturnObject {
    public Object object = null;
    public boolean isDefault = false;
    public ReturnObject(Object obj){
        this.object = obj;
    }

    public ReturnObject(Boolean isDefault){
        this.isDefault = isDefault;
    }

    public Object get(){
        if(isDefault) return 0;
        if(object == null) return 0;
        return object;
    }

    public String toString(){
        if(isDefault) return "0";
        if(object == null) return "Zero Return Object";
        return String.valueOf(object);
    }
}
