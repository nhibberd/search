package main.service.query;

import main.data.core.Function;
import main.data.core.Status;
import main.data.file.Documents;
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



    public String document(final String query){
        return connector.withConnection(new Function<Connection, String>() {
            public String apply(final Connection connection) {
                List<Score> end = new ArrayList<Score>();
                String re = "";
                Integer highrank = 0;

                if(indexDb.exists(connection, query) == Status.BAD_REQUEST){
                    List<Id> ids = getIds(connection,query);

                    for (Id id : ids) {
                        if ((rankDb.exists(connection, id.id_file) == Status.BAD_REQUEST) && (fileDb.exists(connection, id.id_file) == Status.BAD_REQUEST))
                        end.add(new Score(id.id_file, fileDb.get(connection, id.id_file).url,
                                rankDb.get(connection,id.id_file)));
                    }
                }

                for (Score data : end) {
                    if (data.score > highrank){
                        re = data.url;
                        highrank = data.score;
                    }
                }

                return re;
            }
        });
    }



    /*
    todo
    Single: check for search term. get documents attached, get rank of documents, produce ranked list

    Multi: iterate(check term, get documents, get rank, add to list ), produce ranked list from all terms.
            ||   only get documents which have all terms?


     */
}
