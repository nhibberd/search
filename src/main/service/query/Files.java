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
import static main.tool.Database.connector;

public class Files {
    private FileDb fileDb = new FileDb();
    private RankDb rankDb = new RankDb();

    /***
     *
     * @param query search terms
     * @return splits terms on white space
     */
    public String[] multiQuery(final String query) {
        return query.split("\\s");
    }


    /**
     *
     * @param query file name to search
     * @return Absolute path to file
     */
    public String top(final String query){
        return connector.withConnection(new Function<Connection, String>() {
            public String apply(final Connection connection) {
                List<Score> end = new ArrayList<Score>();
                String[] d = multiQuery(query);

                for (String word : d) {
                    List<Id> ids = getIds(connection,word);
                    for (Id id : ids) {
                        Result<Score> tmp = file(id.id_file);
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
        });
    }

    /**
     *
     * @param query file name to search
     * @return List of Absolute path to file
     */
    public List<String> list(final String query){
        return connector.withConnection(new Function<Connection, List<String>>() {
            public List<String> apply(final Connection connection) {
                List<String> end = new ArrayList<String>();
                String[] d = multiQuery(query);

                for (String word : d) {
                    List<Id> ids = getIds(connection,word);
                    for (Id id : ids) {
                        Result<Score> tmp = file(id.id_file);
                        if (tmp.statusOK() )
                            if (!end.contains(tmp.value().url))
                                end.add(tmp.value().url);
                    }
                }


                if (!(end.size() > 0))
                    end.add("No result's");
                return end;
            }
        });
    }



    /**
     *
     * @param query file name to search
     * @param size size of list to return
     * @return List of Absolute path to file
     */

    public List<String> list(final String query, final Integer size){
        return connector.withConnection(new Function<Connection, List<String>>() {
            public List<String> apply(final Connection connection) {
                List<Score> big = new ArrayList<Score>();
                List<String> end = new ArrayList<String>();
                String[] d = multiQuery(query);

                for (String word : d) {
                    List<Score> scores = new ArrayList<Score>();

                    List<Id> ids = getIds(connection,word);
                    System.out.println("ids.size() = " + ids.size());

                    for (Id id : ids) {
                        System.out.println("id.id_file = " + id.id_file);
                        Result<Score> tmp = file(id.id_file);
                        if (tmp.statusOK() )  {
                            if (!scores.contains(tmp.value()))
                                scores.add(tmp.value());
                        }
                    }
                    System.out.println("scores = " + scores.size());

                    big.addAll(scores);
                }
                System.out.println("big.size() = " + big.size());

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
        });
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
            if (data.score > highrank){
                re = data;
                highrank = data.score;

            }
        }
        if (highrank>-1)
            return Result.ok(re);
        return Result.notfound();
    }

    /**
     *
     * @param file File ID
     * @return Result of Score
     */
    private Result<Score> file(final Integer file){
        return connector.withConnection(new Function<Connection, Result<Score>>() {
            public Result<Score> apply(final Connection connection) {
                if ((rankDb.exists(connection, file) == Status.BAD_REQUEST) && (fileDb.exists(connection, file) == Status.BAD_REQUEST)){
                    return Result.ok(new Score(file, fileDb.get(connection, file).url, rankDb.getScore(connection, file)));
                }
                return Result.notfound();
            }
        });
    }
}
