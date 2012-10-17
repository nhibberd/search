package main.tool;

import java.util.ArrayList;
import java.util.List;

public class Parsers {

    public static List<Integer> splitToInt(String data){
        List<Integer> r = new ArrayList<Integer>();
        String delims = "[,]";
        String[] tokens = data.split(delims);
        for (String token : tokens) {
            r.add(Integer.parseInt(token));
        }
        return r;
    }
}
