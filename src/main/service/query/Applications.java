package main.service.query;

import main.data.core.Function;
import main.data.core.Result;
import main.data.core.Status;
import main.data.index.Id;
import main.data.rank.Score;
import main.service.file.FileDb;
import main.service.rank.RankDb;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static main.service.query.Index.getIds;
import static main.service.rank.RankFunctions.isApplication;
import static main.service.rank.RankFunctions.isDocument;
import static main.tool.Database.connector;

public class Applications {
    private FileDb fileDb = new FileDb();
    private RankDb rankDb = new RankDb();

    /***
     *
     * @param query search terms
     * @return splits terms on white space
     */
    private String[] multiQuery(final String query) {
        return query.split("\\s");
    }

    /**
     *
     * @param query application name to search
     * @return Absolute path to application
     */
    public String top(Connection connection, final String query){
        List<Score> end = new ArrayList<Score>();
        String[] d = multiQuery(query);

        for (String word : d) {
            List<Score> scores = new ArrayList<Score>();
            List<Id> ids = getIds(connection,word);
            for (Id id : ids) {
                Result<Score> tmp = application(connection, id.id_file);
                if (tmp.statusOK() )
                    end.add(tmp.value());
            }
        }


        String r = "";
        Result<Score> q = top(end);
        if (q.statusOK())
            r = q.value().url;
        if (r.equals(""))
            return "No result's";
        else
            return r;
    }

    /**
     *
     * @param query application name to search
     * @return List of Absolute path to application
     */
    public List<String> list(Connection connection, final String query){
        List<String> end = new ArrayList<String>();
        String[] d = multiQuery(query);

        for (String word : d) {
            List<Score> scores = new ArrayList<Score>();
            List<Id> ids = getIds(connection,word);
            for (Id id : ids) {
                Result<Score> tmp = application(connection, id.id_file);
                if (tmp.statusOK() ) {
                    String url = tmp.value().url;
                    if (isApplication(fileDb.get(connection, id.id_file).permissions)){
                        if (!end.contains(url))
                            end.add(url);
                    }
                }
            }
        }

        if (!(end.size() > 0))
            end.add("No result's");
        return end;
    }



    /**
     *
     * @param query application name to search
     * @param size size of list to return
     * @return List of Absolute path to application
     */

    public List<String> list(Connection connection, final String query, final Integer size){
        List<Score> big = new ArrayList<Score>();
        List<String> end = new ArrayList<String>();
        String[] d = multiQuery(query);

        for (String word : d) {
            List<Score> scores = new ArrayList<Score>();

            List<Id> ids = getIds(connection,word);

            for (Id id : ids) {
                Result<Score> tmp = application(connection, id.id_file);
                if (tmp.statusOK() )  {
                    if (isApplication(fileDb.get(connection, id.id_file).permissions)){
                        if (!scores.contains(tmp.value()))
                            scores.add(tmp.value());
                    }
                }
            }

            big.addAll(scores);
        }

        if (!(big.size() > 0))
            end.add("No result's");
        if (big.size() <= size){
            for (Score score : big) {
                end.add(score.url);
            }
        } else if (big.size() > size){
            for (int i = 0; i < size; i++){
                Result<Score> r = top(big);
                if (r.statusOK()){
                    end.add(r.value().url);
                    big.remove(r.value());
                }
            }
        }
        return end;
    }


    /**
     *
     * @param input List of data.main.rank.Score
     * @return option of top score
     */
    private Result<Score> top(List<Score> input){
        Score re = null;
        Integer highrank = -1;
        for (Score data : input) {
            if (isApp(data.id_file) == Status.OK){
                if (data.score > highrank){

                    re = data;
                    highrank = data.score;

                }
            }
            if (data.score > highrank){
                re = data;
                highrank = data.score;

            }
        }
        if (highrank>-1)
            return Result.ok(re);
        return Result.notfound();
    }

    private Status isApp(final Integer id){
        return connector.withConnection(new Function<Connection, Status>() {
            public Status apply(final Connection connection) {
                return (isApplication(fileDb.get(connection, id).permissions)) ? Status.OK : Status.BAD_REQUEST;
            }
        });
    }

    /**
     *
     * @param file File ID
     * @return Result of Score
     */
    private Result<Score> application(Connection connection, final Integer file){
        if ((rankDb.exists(connection, file) == Status.BAD_REQUEST) && (fileDb.exists(connection, file) == Status.BAD_REQUEST))
            return Result.ok(new Score(file, fileDb.get(connection, file).url, rankDb.getScore(connection, file)));
        return Result.notfound();
    }
}
