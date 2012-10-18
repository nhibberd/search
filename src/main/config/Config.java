package main.config;

public class Config {
    public String dblocation;
    public String dbuser;
    public String dbpassword;


    public Config(String dblocation, String dbuser, String dbpassword) {
        this.dblocation = dblocation;
        this.dbuser = dbuser;
        this.dbpassword = dbpassword;
    }
}
