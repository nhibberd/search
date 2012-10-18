package main.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigSetup {
    private static Properties configFile = new Properties();

    public static void set(File file) {
        try {
            configFile.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void check(Object... args) {
        for (Object arg : args)
            if (arg == null)
                throw new IllegalArgumentException("Unexpected null constructing config");
    }

    public Config stuff() {
        String dblocation = configFile.getProperty("dblocation");
        String dbuser = configFile.getProperty("dbuser");
        String dbpassword = configFile.getProperty("dbpassword");

        check(dblocation, dbuser, dbpassword);

        return new Config(dblocation, dbuser, dbpassword);
    }
}
