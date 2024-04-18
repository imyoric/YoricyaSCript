package ru.yoricya.ysc;

import ru.yoricya.ysc.Classes.Classes;
import ru.yoricya.ysc.LangObjects.*;
import ru.yoricya.ysc.Modules.LangArrays;
import ru.yoricya.ysc.Modules.Modules;
import ru.yoricya.ysc.YscFiles.DefaultFile;
import ru.yoricya.ysc.YscFiles.YscClass;
import ru.yoricya.ysc.YscFiles.YscModule;
import ru.yoricya.ysc.YscFiles.YscScript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class YscParseUtils {

    public static short getFileType(String script){
        byte[] bytes = script.getBytes();
        replaceComment(bytes, 0);

        if(isNextCharsEquals(bytes, 0, "class")) return DefaultFile.CLASS_FILE_TYPE;
        if(isNextCharsEquals(bytes, 0, "module")) return DefaultFile.MODULE_FILE_TYPE;
        return DefaultFile.SCRIPT_FILE_TYPE;
    }
    public static DefaultFile ParseFunctionalSpace(Ysc ysc, String script){
        byte[] bytes = script.getBytes();
        replaceComment(bytes, 0);

        if(ysc == null) ysc = new DefaultFile();

        boolean isOpenStrBlock = false;
        for(int i = 0; i < bytes.length-1; i++){
            if(bytes[i] == ' ') continue;
            if(bytes[i] == '\n') continue;
            if(bytes[i] == ';') continue;

            if(bytes[i] == '"') isOpenStrBlock = !isOpenStrBlock;
            if(isOpenStrBlock) continue;

            if(isNextCharsEquals(bytes, i, "class")){
                i+=5;

                String classpath = getNextString(bytes, i, ';');
                i += classpath.length();

                ysc = new YscClass(classpath.trim());
            }else if(isNextCharsEquals(bytes, i, "module")){
                i+=6;

                String classpath = getNextString(bytes, i, ';');
                i += classpath.length();

                ysc = new YscModule(classpath.trim());
            }else if(isNextCharsEquals(bytes, i, "var")){
                i+=3;

                String varName = getNextString(bytes, i, '=');
                i += varName.length()+1;

                String varData = getNextString(bytes, i, ';');
                i += varData.length();

                Object object = parseVar(ysc, varData.trim());

                if(object != null) ysc.putVar(varName.trim(), object);
            }else if(isNextCharsEquals(bytes, i, "func")){
                i+=4;

                String funcName = getNextString(bytes, i, '{');
                i+=funcName.length();

                String funcBody = parseCodeBlock(bytes, i);
                i+= funcBody.length();

                Function f = new Function(funcBody);
                f.FuncName = funcName.trim();

                ysc.putVar(funcName.trim(), f);
            }else if(isNextCharsEquals(bytes, i, "import")){
                i+=6;

                String importName = getNextString(bytes, i, ';');
                i+=importName.length()+1;

                importName = importName.trim();

                Modules.Module module = Modules.get(importName);

                if(module == null)
                    throw new RuntimeException("Unknown module '"+importName+"'!");

                ysc.putVar(module.classPath, module);

                for(Function func: module.getAllFuncs()){
                    ysc.putVar(module.classPath+"->"+func.FuncName, func);
                }
            }
        }

        if(ysc instanceof DefaultFile) {
            return (DefaultFile) ysc;
        }
        else
            return new YscScript(ysc);
    }

    public static ReturnObject parseFunc(Ysc ysc, String functionBody, Object[] args){
        byte[] bytes = functionBody.getBytes();
        if(bytes[0] == '{') bytes[0] = 0;
        if(bytes[bytes.length-1] == '}')  bytes[bytes.length-1] = 0;
        bytes = new String(bytes).trim().getBytes();

        Ysc isolatedYsc = new Ysc();
        isolatedYsc.reInitObjects(new LinkVariableMap(ysc.getVariableMap()));

        Classes.Class thisClass = new Classes.Class("This");
        isolatedYsc.putVar("this", thisClass, true);

        thisClass.putVar("args", new Classes.Array(args));

        boolean isOpenStrBlock = false;
        for(int i = 0; i < bytes.length-1; i++){
            if(bytes[i] == ' ') continue;
            if(bytes[i] == '\n') continue;
            if(bytes[i] == ';') continue;

            if(bytes[i] == '"') isOpenStrBlock = !isOpenStrBlock;
            if(isOpenStrBlock) continue;

            if(isNextCharsEquals(bytes, i, "var")){
                i+=3;

                String varName = "";

                while(varName.trim().isEmpty()){
                    varName = getNextString(bytes, i, ' ');
                    i += varName.length()+1;
                }
                varName = varName.trim();

                String varData = getNextString(bytes, i, ';');
                i += varData.length()+1;

                varData = varData.trim();

                varWorking(isolatedYsc, varName, varData);
            }else if(isNextCharsEquals(bytes, i, "return")){
                i+=6;

                String returnData = getNextString(bytes, i, ';');

                if(returnData.trim().isEmpty()) return null;

                return new ReturnObject(parseVar(isolatedYsc, returnData.trim()));
            }else if(isNextCharsEquals(bytes, i, "if")){
                i+=2;

                String ifBody = getNextString(bytes, i, '{');
                i += ifBody.length();

                String body = parseCodeBlock(bytes, i);
                i+=body.length();

                String elseBody = "";
                boolean isElse = false;
                if(isNextCharsEquals(bytes, i, "else")){
                    i+=4;
                    isElse = true;
                    elseBody = parseCodeBlock(bytes, i);
                    i+=body.length();
                }

                String[] s = parseIfBody(ifBody.trim());
                ReturnObject returnObject = null;
                if(WorkIfCondition(parseVar(isolatedYsc, s[0]), parseVar(isolatedYsc, s[2]), s[1]))
                    returnObject = parseFunc(isolatedYsc, body, args);
                else if(isElse)
                    returnObject = parseFunc(isolatedYsc, elseBody, args);

                if(returnObject != null && !returnObject.isDefault) return returnObject;
            }else if(isNextCharsEquals(bytes, i, "while")){
                i+=5;

                String ifBody = getNextString(bytes, i, '{');
                i += ifBody.length();

                String body = parseCodeBlock(bytes, i);
                i+=body.length();

                String[] s = parseIfBody(ifBody.trim());
                while (WorkIfCondition(parseVar(isolatedYsc, s[0]), parseVar(isolatedYsc, s[2]), s[1])) {
                    ReturnObject returnObject =  parseFunc(isolatedYsc, body, args);
                    if(returnObject != null && !returnObject.isDefault) return returnObject;
                }
            }else{
                String microFuncBody = getNextString(bytes, i, ';');
                i += microFuncBody.length()+1;

                parseMicroFuncBody(isolatedYsc, microFuncBody);
            }
        }

        return new ReturnObject(true);
    }

    static void replaceComment(byte[] array, int pos){
        boolean isComment = false;
        for(int i = pos; i!=array.length; i++){
            if(array[i] == '/' && array.length > i+1 && array[i+1] == '/') isComment = true;
            if(array[i] == '\n') isComment = false;
            if(isComment) array[i] = ' ';
        }
    }

    static void varWorking(Ysc isolatedYsc, String varName, String varData){
        int current = 0;
        String[] funcsChain = splitSkippedSyntaxSym(varName, '.');

        for(String var: funcsChain) {
            current++;
            Object obj = isolatedYsc.getVar(var);

            if(obj instanceof Classes.Class) {
                isolatedYsc = (Ysc) obj;
            }
            else if(funcsChain.length != 1 && current != funcsChain.length){
                throw new RuntimeException("Return value of '"+var+"' <- not contains LangClass!");
            }
            else if(funcsChain.length == current)
                varName = var;
        }

        if(varData.startsWith("=")){
            varData = varData.replaceFirst("=", "");
            Object object = parseVar(isolatedYsc, varData.trim());

            if(object == null) {
                isolatedYsc.putVar(varName.trim(), null);
                return;
            }

            if(object.getClass().isArray()){
                Object[] objArr = getArrayOfObject(object);
                if(objArr == null) throw new RuntimeException("Return value of '"+varName+"' <- not supported array!");

                Classes.Array c = new Classes.Array(objArr.length);

                int cur = 0;
                for(Object o: objArr){
                    if(o == null) o = new LangArrays.EmptyElement();
                    c.putVar(""+cur, o);
                    cur++;
                }

                object = c;
            }

            isolatedYsc.putVar(varName.trim(), object);
        }else if(varData.startsWith("+=")){
            varData = varData.replaceFirst("\\+=", "");

            Object d1 = parseVar(isolatedYsc, varData.trim());
            Object d2 = isolatedYsc.getVar(varName);

            Object d3 = null;
            if(d1 instanceof Long){
                d3 = (long) d2 + (long) d1;
            }else if(d1 instanceof Double){
                d3 = (Double) d2 + (Double) d1;
            }else if(d1 instanceof Integer){
                d3 = ((Number) d2).longValue() + ((Number) d1).longValue();
            }

            isolatedYsc.putVar(varName.trim(), d3);
        }else if(varData.startsWith("-=")){
            varData = varData.replaceFirst("-=", "");
            Object d1 = parseVar(isolatedYsc, varData.trim());
            Object d2 = isolatedYsc.getVar(varName);

            Object d3 = null;
            if(d1 instanceof Long){
                d3 = (long) d2 - (long) d1;
            }else if(d1 instanceof Double){
                d3 = (Double) d2 - (Double) d1;
            }else if(d1 instanceof Integer){
                d3 = ((Number) d2).longValue() - ((Number) d1).longValue();
            }

            isolatedYsc.putVar(varName.trim(), d3);
        }else if(varData.startsWith("*=")){
            varData = varData.replaceFirst("\\*=", "");
            Object d1 = parseVar(isolatedYsc, varData.trim());
            Object d2 = isolatedYsc.getVar(varName);

            Object d3 = null;
            if(d1 instanceof Long){
                d3 = (long) d2 * (long) d1;
            }else if(d1 instanceof Double){
                d3 = (Double) d2 * (Double) d1;
            }else if(d1 instanceof Integer){
                d3 = ((Number) d2).longValue() * ((Number) d1).longValue();
            }

            isolatedYsc.putVar(varName.trim(), d3);
        }else if(varData.startsWith("/=")){
            varData = varData.replaceFirst("/=", "");
            Object d1 = parseVar(isolatedYsc, varData.trim());
            Object d2 = isolatedYsc.getVar(varName);

            Object d3 = null;
            if(d1 instanceof Long){
                d3 = (long) d2 / (long) d1;
            }else if(d1 instanceof Double){
                d3 = (Double) d2 / (Double) d1;
            }else if(d1 instanceof Integer){
                d3 = ((Number) d2).longValue() / ((Number) d1).longValue();
            }

            isolatedYsc.putVar(varName.trim(), d3);
        }
    }

    static String[] parseIfBody(String ifBody){
        byte[] bytes = ifBody.getBytes();
        bytes[0] = 0;
        bytes[bytes.length-1] = 0;

        bytes = removeNoNeededSpaces(bytes);

        String[] splited = new String(bytes).trim().split(" ");

        if(splited.length < 3) throw new RuntimeException("if method syntax error!");

        return splited;
    }

    static boolean WorkIfCondition(Object arg1, Object arg2, String comparator){
        if(arg1 instanceof Number && arg2 instanceof Number)
            return LangNumber.compareNumbers((Number) arg1,(Number) arg2, comparator);

        switch (comparator) {
            case "==":
                return Objects.equals(arg1, arg2);
            case "!=":
                return !Objects.equals(arg1, arg2);
        }

        return false;
    }

    static byte[] removeNoNeededSpaces(byte[] arr){
        byte[] parsedBytes = new byte[arr.length];

        boolean space = false;
        int parsedI = 0;
        for(int i = 0; i != arr.length; i++){
            if(arr[i] != ' ') space = false;
            if(arr[i] == ' ' && space) continue;
            if(arr[i] == ' ') space = true;

            parsedBytes[parsedI] = arr[i];
            parsedI++;
        }

        return parsedBytes;
    }

    static String[] splitSkippedSyntaxSym(String str, char regex){
        List<String> list = new ArrayList<>();

        byte[] bytes = str.getBytes();

        String cache = "";
        boolean isOpenStrBlock = false;
        int quot = 0;

        for(byte aByte : bytes) {
            if(aByte == '"') isOpenStrBlock = !isOpenStrBlock;
            if(aByte == '(') quot++;
            if(aByte == ')') quot--;

            if (aByte == regex && !isOpenStrBlock && quot == 0) {
                list.add(cache);
                cache = "";
                continue;
            }

            cache += (char) aByte;
        }

        if(!cache.isEmpty()) list.add(cache);

        return list.toArray(new String[0]);
    }

    static Object parseMicroFuncBody(Ysc ysc, String microFuncBody){
        String[] funcsChain = splitSkippedSyntaxSym(microFuncBody, '.');

        Ysc currentClass = ysc;

        int current = 0;
        for(String func: funcsChain){
            current++;

            byte[] bytes = func.getBytes();

            int i = 0;
            String funcName = getNextString(bytes, i, '(');
            i += funcName.length();

            if(currentClass.getVar(funcName) instanceof Ysc){
                currentClass = (Ysc) currentClass.getVar(funcName);
                continue;
            }

            if(!(currentClass.getVar(funcName) instanceof Function)) {
                throw new RuntimeException(funcName + " - is not a function!");
            }

            Function function = (Function) currentClass.getVar(funcName);

            String argsBody = getNextStringByInvertedScan(bytes, i, '(', ')');

            byte[] argsBodybByte = argsBody.getBytes();
            argsBodybByte[0] = 0;
            argsBodybByte[argsBodybByte.length-1] = 0;
            String[] argsBodyArr = new String(argsBodybByte).trim().split(",");

            Object[] workedArgs = new Object[argsBodyArr.length];

            for(int a = 0; a != argsBodyArr.length; a++){
                if(argsBodyArr[a].isEmpty()) continue;
                workedArgs[a] = parseVar(currentClass, argsBodyArr[a]);
            }

            if(Arrays.equals(workedArgs, new Object[argsBodyArr.length])){
                workedArgs = new Object[0];
            }

            if(function instanceof NativeFunction){
                Object re = ((NativeFunction) currentClass.getVar(funcName)).run(currentClass, workedArgs);

                if(re instanceof Ysc){
                    currentClass = (Ysc) re;
                    if(current == funcsChain.length) return currentClass;
                }else if(funcsChain.length != 1 && current != funcsChain.length){
                    throw new RuntimeException("Return value of '."+func+"' <- not contains LangClass!");
                }else{
                    return re;
                }
                continue;
            }

            Object re = parseFunc(currentClass, function.Body, workedArgs).get();
            if(re instanceof Ysc){
                currentClass = (Ysc) re;
                if(current == funcsChain.length) return currentClass;
            }else if(funcsChain.length != 1 && current != funcsChain.length){
                throw new RuntimeException("Return value of '"+func+".' <- not contains LangClass!");
            }else{
                return re;
            }
        }

        return null;
    }

    static String parseCodeBlock(byte[] array, int startWith){
        int countQuotes = 0;

        String body = "";

        while(true){
            body += (char) array[startWith];

            if(array[startWith] == '{') countQuotes++;
            if(array[startWith] == '}') countQuotes--;
            if(countQuotes == 0) break;
            startWith++;
        }

        return body;
    }

    static String getNextStringByInvertedScan(byte[] array, int startWith, char startSym, char endSym){
        String s = "";

        int countQuotes = 0;
        boolean isOpenStrBlock = false;
        startWith--;
        while(startWith != array.length-1){
            startWith++;
            if(array[startWith] == '"') isOpenStrBlock = !isOpenStrBlock;

            s += (char) array[startWith];

            if(!isOpenStrBlock){
                if(array[startWith] == startSym ) countQuotes++;
                if(array[startWith] == endSym) countQuotes--;
                if(countQuotes == 0) break;
            }
        }

        return s;
    }

    static String getNextString(byte[] array, int startWith, char endSym){
        String s = "";

        startWith--;
        boolean isOpenStrBlock = false;
        while(startWith != array.length-1){
            startWith++;

            if(array[startWith] == '"') isOpenStrBlock = !isOpenStrBlock;
            if(array[startWith] == endSym && !isOpenStrBlock) return s;

            s += (char) array[startWith];
        }

        return s;
    }

    static Boolean parseBoolean(String str){
        if(isNextCharsEquals(str.trim().getBytes(), 0,"false"))
            return false;
        else if(isNextCharsEquals(str.trim().getBytes(), 0,"true"))
            return true;
        else return null;
    }

    static Object parseVar(Ysc ysc, String str){
        str = str.trim();
        try{
            return Integer.parseInt(str);
        }catch (Exception ignore){}

        try{
            return Double.parseDouble(str);
        }catch (Exception ignore1){}

        try{
            Boolean a = parseBoolean(str);
            if(a != null) return a;
        }catch (Exception ignore1){}

        if (str.startsWith("\"")) {
            byte[] bs = str.getBytes();
            if(bs[0] == '"' && bs[bs.length-1] == '"'){
                bs[0] = 0;
                bs[bs.length-1] = 0;
                return textParse(ysc, new String(bs).trim());
            }else throw new RuntimeException("Invalid string syntax!");
        }else if(str.endsWith(")")){
            int current = 0;
            String[] funcsChain = splitSkippedSyntaxSym(str, '.');

            for(String var: funcsChain) {
                current++;
                Object obj = ysc.getVar(var);

                if(obj instanceof Classes.Class) {
                    ysc = (Ysc) obj;
                }
                else if(funcsChain.length != 1 && current != funcsChain.length)
                    throw new RuntimeException("Return value of '."+var+"' <- not contains LangClass!");
                else if(funcsChain.length == current)
                    str = var;
            }

            return parseMicroFuncBody(ysc, str);
        }else if(str.contains("[") && str.contains("]")){
            String s = parseSquareBrackets(str).trim();
            String s1 = String.valueOf(parseVar(ysc, s)).trim();
            str = str.replace("["+s+"]", s1);

            int current = 0;
            String[] funcsChain = splitSkippedSyntaxSym(str, '.');

            for(String var: funcsChain) {
                current++;
                Object obj = ysc.getVar(var);

                if(funcsChain.length == 1) return obj;

                if(obj instanceof Classes.Class)
                    ysc = (Ysc) obj;
                else if(current != funcsChain.length)
                    throw new RuntimeException("Return value of '."+var+"' <- not contains LangClass!");
                else
                    str = var;
            }

            return ysc.getVar(str);
        }else{
            int current = 0;
            String[] funcsChain = splitSkippedSyntaxSym(str, '.');

            for(String var: funcsChain) {
                current++;

//                try {
//                    System.out.println((Arrays.toString(((Classes.Array) ysc).Array)));
//                }catch (Exception e){}
                Object obj = ysc.getVar(var);

                if(funcsChain.length == 1) return obj;

                if(obj instanceof Classes.Class)
                    ysc = (Ysc) obj;
                else if(current != funcsChain.length)
                    throw new RuntimeException("Return value of '."+var+"' <- not contains LangClass!");
                else
                    str = var;
            }

            return ysc.getVar(str);
        }

    }

    static Object[] getArrayOfObject(Object obj){
        if (obj instanceof Object[]) {
            return (Object[]) obj;
        } else if (obj instanceof byte[]) {
            byte[] byteArray = (byte[]) obj;
            Object[] objectArray = new Object[byteArray.length];
            for (int i = 0; i < byteArray.length; i++) {
                objectArray[i] = byteArray[i];
            }
            return objectArray;
        } else if (obj instanceof int[]) {
            int[] intArray = (int[]) obj;
            Object[] objectArray = new Object[intArray.length];
            for (int i = 0; i < intArray.length; i++) {
                objectArray[i] = intArray[i];
            }
            return objectArray;
        } else if (obj instanceof float[]) {
            float[] intArray = (float[]) obj;
            Object[] objectArray = new Object[intArray.length];
            for (int i = 0; i < intArray.length; i++) {
                objectArray[i] = intArray[i];
            }
            return objectArray;
        } else if (obj instanceof double[]) {
            double[] intArray = (double[]) obj;
            Object[] objectArray = new Object[intArray.length];
            for (int i = 0; i < intArray.length; i++) {
                objectArray[i] = intArray[i];
            }
            return objectArray;
        } else if (obj instanceof short[]) {
            short[] intArray = (short[]) obj;
            Object[] objectArray = new Object[intArray.length];
            for (int i = 0; i < intArray.length; i++) {
                objectArray[i] = intArray[i];
            }
            return objectArray;
        } else if (obj instanceof long[]) {
            long[] intArray = (long[]) obj;
            Object[] objectArray = new Object[intArray.length];
            for (int i = 0; i < intArray.length; i++) {
                objectArray[i] = intArray[i];
            }
            return objectArray;
        } else if (obj instanceof char[]) {
            char[] intArray = (char[]) obj;
            Object[] objectArray = new Object[intArray.length];
            for (int i = 0; i < intArray.length; i++) {
                objectArray[i] = intArray[i];
            }
            return objectArray;
        }

        return null;
    }

    static String textParse(Ysc ysc, String str) {
        byte[] array = str.getBytes();
        List<String> toReplace = new ArrayList<>();

        for(int i = 0; i<array.length; i++){
            if(array[i] == '$'){
                if(i > 1) if(array[i-1] == '\\') continue;
                i++;
                String st = getNextString(array, i,'$');
                i += st.length()+1;
                toReplace.add(st);
            }

            if(i >= array.length) break;
        }

        for(String s: toReplace)
            str = str.replace("$" + s + "$", String.valueOf(parseVar(ysc, s)));

        str = str.replace("\\$", "$").replace("\\n", "\n");

        return str;
    }

    static String parseSquareBrackets(String str){
        byte[] array = str.getBytes();
        String s = "";

        int Q = 0;
        for(int i = 0; i!=array.length; i++){
            if(array[i] == '[') {
                Q++;
                if(Q == 1) continue;
            }

            if(array[i] == ']') Q--;

            if(Q == 0) continue;

            s += (char) array[i];
        }

        return s;
    }

    static boolean isNextCharsEquals(byte[] array, int pos, String str){
        byte[] bytes = str.getBytes();
        int nextChars = bytes.length;

        if(pos+nextChars > array.length) return false;

        int i = 0;
        boolean is = true;
        while(i != nextChars){
            if(array[pos] != bytes[i]) is = false;

            i++;
            pos++;
        }

        return is;
    }
}
