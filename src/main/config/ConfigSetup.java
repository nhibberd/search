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
        String directory = configFile.getProperty("directory");
        String webdirectory = configFile.getProperty("webdirectory");
        String mfrom = configFile.getProperty("mailfrom");
        String muser = configFile.getProperty("mailuser");
        String mpassword = configFile.getProperty("mailpassword");
        String msmtp = configFile.getProperty("mailhost");
        Integer mport = Integer.valueOf(configFile.getProperty("mailport"));
        String dblocation = configFile.getProperty("dblocation");
        String dbuser = configFile.getProperty("dbuser");
        String dbpassword = configFile.getProperty("dbpassword");
        String contactemail = configFile.getProperty("contactemail");
        String url = configFile.getProperty("url");

        check(dblocation, dbuser, dbpassword, msmtp, mpassword, muser, mfrom, mport, directory, webdirectory, contactemail, url);

        return new Config(dblocation, dbuser, dbpassword, msmtp, mpassword, muser, mfrom, mport, directory, webdirectory, contactemail, url);
    }
}
