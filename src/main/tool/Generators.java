package main.tool;

import java.util.Date;

public class Generators {
    public static String generatorToken(){
        return java.util.UUID.randomUUID().toString();
    }

    public static long generatorAge(){
        Date date = new Date();
        return date.getTime();
    }
}
