package ru.yoricya.ysc;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class LinkVariableMap {
    static final HashMap<String, Object> mapByLinks = new HashMap<>();
    public final HashMap<String, String> LocalMapByName = new HashMap<>();
    public LinkVariableMap(){}
    public LinkVariableMap(LinkVariableMap other){
        for(String key: other.LocalMapByName.keySet()){
            if(!key.startsWith("this")) LocalMapByName.put(key, other.LocalMapByName.get(key));
        }
    }

    public void putWithNewLink(String var, Object data){
        String url = randomUrl(data);
        LocalMapByName.put(var, url);

        LinkVariableMap.mapByLinks.put(url, data);
    }
    public void put(String var, Object data){

        String url = LocalMapByName.get(var);

        if(url == null){
            url = randomUrl(data);
            LocalMapByName.put(var, url);
        }

        LinkVariableMap.mapByLinks.put(url, data);
    }

    public Set<String> keyset(){
        return LocalMapByName.keySet();
    }

    public Object get(String var){
        String url = LocalMapByName.get(var);
        if(url == null) return null;

        return LinkVariableMap.mapByLinks.get(url);
    }

    public String randomUrl(Object object) {
        byte[] array = new byte[32];
        new Random().nextBytes(array);

        if(object != null)
            array[8] = ((Integer) object.hashCode()).byteValue();

        String base = Base64.encode(array);

        if(LinkVariableMap.mapByLinks.get(base) != null)
            base = randomUrl(object);

        return base;
    }
}
