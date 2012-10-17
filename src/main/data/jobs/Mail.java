package main.data.jobs;

public class Mail {
    public String email;
    public String subject;
    public String content;
    public Integer id;

    public Mail(String email, String subject, String content, Integer id) {
        this.email = email;
        this.subject = subject;
        this.content = content;
        this.id = id;
    }

}
