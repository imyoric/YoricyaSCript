package ru.yoricya.ysc;

import ru.yoricya.ysc.Modules.LangFileUtils;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        boolean isBench = false;

        for(int i = 0; i<args.length; i++){
            if(args[i].equals("-yscp") && args.length-i > 1){
                Ysc ysc = new Ysc().loadProjectDir(args[i+1]);
                Object w = ysc.runMain(args);

                if(isBench)
                    System.err.println("[INTERPRETER] Script success ended by "+(System.currentTimeMillis()-startTime)+"ms");

                if(w instanceof Integer)
                    System.exit((int) w);
                else
                    System.exit(0);
            }else if(args[i].equals("-ysc") && args.length-i > 1){
                Ysc ysc = new Ysc();
                String script = LangFileUtils.readFile(args[i+1]);

                if(isBench)
                    System.err.println("[INTERPRETER] Script success ended by "+(System.currentTimeMillis()-startTime)+"ms");

                System.exit(ysc.parse(script));
            }else if(args[i].equals("-bench") && args.length-i > 1){
                isBench = true;
            }
        }

        System.out.println("Ysc interpreter. "+Ysc.YSC_VERSION);
        System.out.println("Usage: -bench(Optional) -yscp <path to project directory>");
        System.out.println("or: -bench(Optional) -ysc <path to .ysc file>");

    }
}