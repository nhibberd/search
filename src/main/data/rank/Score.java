package main.data.rank;

public class Score {
    public Integer id_file;
    public String url;
    public Integer score;

    public Score(Integer id_file, Integer score) {
        this.id_file = id_file;
        this.score = score;
    }

    public Score(Integer id_file, String url, Integer score) {
        this.id_file = id_file;
        this.url = url;
        this.score = score;
    }
}
