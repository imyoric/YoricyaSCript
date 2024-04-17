package ru.yoricya.ysc.Modules;


import ru.yoricya.ysc.LangObjects.NativeFunction;
import ru.yoricya.ysc.Ysc;

import java.io.*;

public class LangFileUtils extends Modules.NativeModule {
    public LangFileUtils() {
        super("FileUtils");

        addFunc("writeFile", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 2)
                    throw new RuntimeException("Arguments cant be skipped!");

                if(!(args[0] instanceof String))
                    throw new RuntimeException("Path to file must be String!");

                return writeFile((String) args[0], String.valueOf(args[1]));
            }
        });

        addFunc("isPathExists", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                if(!(args[0] instanceof String))
                    throw new RuntimeException("Path to file must be String!");

                return new File(((String) args[0])).exists();
            }
        });

        addFunc("mkDir", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                if(!(args[0] instanceof String))
                    throw new RuntimeException("Path to file must be String!");

                return new File(((String) args[0])).mkdir();
            }
        });

        addFunc("readFile", new NativeFunction() {
            @Override
            public Object run(Ysc ysc, Object[] args) {
                if(args.length != 1)
                    throw new RuntimeException("Arguments cant be skipped!");

                if(!(args[0] instanceof String))
                    throw new RuntimeException("Path to file must be String!");

                return readFile((String) args[0]);
            }
        });
    }

    public static String readFile(String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder builder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                builder.append(line).append("\n");
            }

            reader.close();
            return builder.toString();
        } catch (Exception ignore) {}

        return null;
    }

    public static boolean writeFile(String path, String data){
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(path));
            w.write(data);
            w.close();
        } catch (Exception ignore) {return false;}
        return true;
    }
}
