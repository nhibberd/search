package main.config;

public class Config {
    public String directory;
    public String webdirectory;
    public String mailfrom;
    public String mailuser;
    public String mpassword;
    public String mailsmtp;
    public Integer mailport;
    public String dblocation;
    public String dbuser;
    public String dbpassword;
    public String contactemail;
    public String url;


    public Config(String dblocation, String dbuser, String dbpassword, String mailsmtp, String mpassword, String mailuser, String mailfrom, Integer mailport, String directory, String webdirectory, String contactemail, String url) {
        this.dbuser = dbuser;
        this.dbpassword = dbpassword;
        this.dblocation = dblocation;
        this.mailsmtp = mailsmtp;
        this.mpassword = mpassword;
        this.mailuser = mailuser;
        this.mailfrom = mailfrom;
        this.mailport = mailport;
        this.directory = directory;
        this.webdirectory = webdirectory;
        this.contactemail = contactemail;
        this.url = url;
    }
}
