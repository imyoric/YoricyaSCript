package ru.yoricya.ysc.LangObjects;

import java.util.Objects;

public class LangNumber {
    Long longValue;
    Double doubleValue;
    public LangNumber(Object numericObject){
        if(numericObject instanceof Integer)
            longValue = ((Integer) numericObject).longValue();

        if(numericObject instanceof Long)
            longValue = ((Long) numericObject);

        if(numericObject instanceof Double)
            doubleValue = ((Double) numericObject);

        if(numericObject instanceof Float)
            doubleValue = ((Float) numericObject).doubleValue();
    }

    public boolean compareValues(String comparator, LangNumber otherNumber){
        if(comparator.equals("==")){
            if(isDouble() && otherNumber.isDouble()){
                return Objects.equals(getDoubleValue(), otherNumber.getDoubleValue());
            }else if(!isDouble() && !otherNumber.isDouble()){
                return Objects.equals(getLongValue(), otherNumber.getLongValue());
            }else return false;
        }

        return false;
    }

    public Double getDoubleValue(){
        if(longValue == null && doubleValue == null)
            return 0d;
        else if(longValue != null)
            return longValue.doubleValue();
        else return doubleValue;
    }

    public boolean isDouble(){
        return doubleValue != null && longValue == null;
    }

    public Long getLongValue(){
        if(longValue == null && doubleValue == null)
            return 0L;
        else if(longValue != null)
            return longValue;
        else
            return doubleValue.longValue();
    }

    public static boolean compareNumbers(Number n1, Number n2, String comparator){
        switch (comparator){
            case "==":
                return Objects.equals(n1.doubleValue(), n2.doubleValue());
            case "!=":
                return !Objects.equals(n1.doubleValue(), n2.doubleValue());
            case ">":
                return n1.doubleValue() > n2.doubleValue();
            case "<":
                return n1.doubleValue() < n2.doubleValue();
            case ">=":
                return n1.doubleValue() >= n2.doubleValue();
            case "<=":
                return n1.doubleValue() <= n2.doubleValue();
        }

        return false;
    }
}
