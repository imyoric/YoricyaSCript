package ru.yoricya.ysc.Modules;

import ru.yoricya.ysc.Classes.Classes;
import ru.yoricya.ysc.LangObjects.NativeFunction;
import ru.yoricya.ysc.Ysc;

public class LangArrays extends Modules.NativeModule {
    public LangArrays() {
        super("Arrays");
        addFunc("newArray", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped! -> func(arrayLength)");

                return new Object[Integer.parseInt(args[0].toString())];
            }
        }.addNeedArg("arrayLength", "Length of array"));

        addFunc("copyArray", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length == 3) {
                    Classes.Array oldArray = (Classes.Array) args[0];
                    Classes.Array newArray = new Classes.Array(Integer.parseInt(String.valueOf(args[2])));

                    int ni = 0;
                    for(int i = Integer.parseInt(String.valueOf(args[1])); i != oldArray.Array.length; i++){
                        newArray.Array[ni] = oldArray.Array[i];
                        ni++;
                    }

                    return newArray;
                }else
                    throw new RuntimeException("Arguments cant be skipped! -> func(oldArray, srcPosition, length)");
            }
        });
    }

    public static class EmptyElement{

        @Override
        public String toString() {
            return null;
        }
    }
}
