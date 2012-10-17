package main.tool;

import main.data.core.Return;
import main.data.core.Status;

import java.util.Date;

public class Validations {
    public static Boolean checkAge(long age, long time){
        Date date = new Date();
        return (date.getTime() < (age + time));
    }

    public static Boolean checkboolrow(int i){
        return i != 0;
    }

    public static Return checkrow(int i){
        if (i != 0)
            return new Return(true,"");
        else
            return new Return(false, "Database error. Please contact an administrator.");
    }

    public static Return checkrow(int i, String errormsg){
        if (i != 0)
            return new Return(true,"");
        else
            return new Return(false, errormsg);
    }

    public static Status checkrownu(int i){
        if (i != 0)
            return Status.OK;
        else
            return Status.NOT_FOUND;
    }

    public static Boolean checkadmin (int i){
        return i == 0 || i == 1 || i == 2;
    }

}
