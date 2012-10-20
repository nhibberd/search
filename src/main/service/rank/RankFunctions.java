package main.service.rank;


import java.util.ArrayList;
import java.util.List;

public class RankFunctions {
    //Assuming document extensions
    public static boolean isDocument(String url){
        return (url.endsWith(".htm") || url.endsWith(".html") || url.endsWith(".txt") ||
                url.endsWith(".xml") || url.endsWith(".properties") || url.endsWith(".doc") || url.endsWith(".pdf") );
    }

    //Assuming an application has +x permissions and is not a directory.
    public static boolean isApplication(Integer permissions){
        List<Integer> q = digits(permissions);
        for (Integer integer : q) {
            if (integer == 1 | integer == 3 | integer == 5 | integer == 7)
                return true;
        }
        return false;

    }

    private static List<Integer> digits(Integer input) {
        List<Integer> r = new ArrayList<Integer>();
        return digits(input, r);
    }

    private static List<Integer> digits(Integer input, List<Integer> data) {
        if (input > 0) {
            data.add(input % 10);
            digits(input/10,data);
        }
        return data;
    }
}
