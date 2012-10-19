package main.tool;

import main.data.core.Status;

import java.util.Date;

public class Validations {
    public static Boolean checkAge(long age, long time){
        Date date = new Date();
        return (date.getTime() < (age + time));
    }

    public static Status checkrow(int i){
        if (i != 0)
            return Status.OK;
        else
            return Status.NOT_FOUND;
    }

}
