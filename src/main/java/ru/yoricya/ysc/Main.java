package ru.yoricya.ysc;

import ru.yoricya.ysc.Modules.LangFileUtils;

public class Main {
    public static void main(String[] args) {
        boolean is = false;
        for(int i = 0; i<args.length; i++){
            if(args[i].equals("-yscp") && args.length-i > 1){
                is = true;
                Ysc ysc = new Ysc().loadProjectDir(args[i+1]);
                Object w = ysc.runMain(args);
                if(w instanceof Integer) System.exit((int) w);
            }else if(args[i].equals("-ysc") && args.length-i > 1){
                is = true;
                Ysc ysc = new Ysc();
                String script = LangFileUtils.readFile(args[i+1]);
                System.exit(ysc.parse(script));
            }
        }

        if(!is){
            System.out.println("Usage: -yscp <path to project directory>");
            System.out.println("or: -ysc <path to .ysc file>");
        }
    }
}