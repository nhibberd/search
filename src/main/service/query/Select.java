package main.service.query;

import main.data.core.Function;
import main.data.core.Result;
import main.data.core.Status;
import main.data.index.Id;
import main.data.rank.Score;
import main.service.file.FileDb;
import main.service.file.StateDb;
import main.service.index.IndexDb;
import main.service.rank.RankDb;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static main.tool.Database.connector;
import static main.service.query.Index.*;

public class Select {
    private StateDb stateDb = new StateDb();
    private FileDb fileDb = new FileDb();
    private IndexDb indexDb = new IndexDb();
    private RankDb rankDb = new RankDb();

    /**
     *
     * @param file File ID
     * @return Result of Score
     */
    private Result<Score> document(final Integer file){
        return connector.withConnection(new Function<Connection, Result<Score>>() {
            public Result<Score> apply(final Connection connection) {
                if ((rankDb.exists(connection, file) == Status.BAD_REQUEST) && (fileDb.exists(connection, file) == Status.BAD_REQUEST))
                    return Result.ok(new Score(file, fileDb.get(connection, file).url, rankDb.getScore(connection, file)));
                return Result.notfound();
            }
        });
    }


    /**
     *
     * @param query document name to search
     * @return Absolute path to document
     */
    public String topDocument(final String query){
        return connector.withConnection(new Function<Connection, String>() {
            public String apply(final Connection connection) {
                List<Score> end = new ArrayList<Score>();
                String re = "";
                Integer highrank = 0;

                List<Id> ids = getIds(connection,query);
                for (Id id : ids) {
                    Result<Score> tmp = document(id.id_file);
                    if (tmp.statusOK() )
                        end.add(tmp.value());
                }

                for (Score data : end) {
                    if (data.score > highrank){
                        re = data.url;
                        highrank = data.score;
                    }
                }
                if (re.equals(""))
                    return "No results";
                else
                    return re;
            }
        });
    }

    /**
     *
     * @param query document name to search
     * @return List of Absolute path to document
     */
    public List<String> listDocuments(final String query){
        return connector.withConnection(new Function<Connection, List<String>>() {
            public List<String> apply(final Connection connection) {
                List<String> end = new ArrayList<String>();

                List<Id> ids = getIds(connection,query);
                for (Id id : ids) {
                    String r = "";
                    Result<Score> tmp = document(id.id_file);
                    if (tmp.statusOK() )
                        r = tmp.value().url;
                    if (!end.contains(r))
                        end.add(r);
                }

                if (!(end.size() > 0))
                    end.add("No resutls");
                return end;
            }
        });
    }



    /*
    todo
    Single: check for search term. getScore documents attached, getScore rank of documents, produce ranked list

    Multi: iterate(check term, getScore documents, getScore rank, add to list ), produce ranked list from all terms.
            ||   only getScore documents which have all terms?


     */
}
