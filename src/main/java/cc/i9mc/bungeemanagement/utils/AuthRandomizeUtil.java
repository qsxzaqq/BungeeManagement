package cc.i9mc.bungeemanagement.utils;

public class AuthRandomizeUtil {
    private static int current;

    public static String get(){
        if(current == 2){
            current = 1;
        }else{
            current++;
        }
        return "Auth-" + current;
    }
}
