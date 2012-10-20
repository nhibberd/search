package main.service.rank;

import main.data.core.Action;
import main.data.core.Status;
import main.data.file.Documents;
import main.data.file.State;
import main.data.rank.Change;
import main.data.rank.Score;
import main.service.file.FileDb;
import main.service.file.StateDb;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static main.data.state.Params.records;
import static main.tool.Database.connector;
import static main.service.rank.RankFunctions.*;

public class Ranker {

    //todo ranking algorithm || File. a=5,m=5,g=2,o=2,p=1 ++ Application a=5,m=1,g=2,o=2,p=2
    public void run() {
        System.out.println("ranking...");
        long start = System.currentTimeMillis();
        connector.withConnection(new Action<Connection>() {
            public void apply(final Connection connection) {
                StateDb stateDb = new StateDb();
                FileDb fileDb = new FileDb();
                RankDb rankDb = new RankDb();

                List<Documents> z = fileDb.getAll(connection);

                List<Score> d = new ArrayList<Score>();


                for (Documents doc : z) {
                    if (stateDb.exists(connection,doc.url) == Status.BAD_REQUEST) {
                        Integer count = stateDb.getCount(connection, doc.url);
                        if (count>0) {
                            State s = stateDb.get(connection,doc.url);
                            d.add(new Score(doc.id, doc.url, score(s, doc.links, count)));
                        }
                    }
                }

                for (Score score : d) {
                    if (rankDb.exists(connection,score.id_file) == Status.OK)
                        rankDb.insert(connection,score);
                    else
                        rankDb.update(connection,score);
                }
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("done rank. Run time: " + (end-start));
    }

    private Integer score(State s, Integer links, Integer count){
        Integer r = 0;
        if(isApplication(s.permissions)){
            if (s.atime>0)
                r = s.atime * 5;
            if (s.mtime>0)
                r = s.mtime * 1;
            if (s.group>0)
                r = s.group * 2;
            if (s.owner>0)
                r = s.owner * 2;
            if (s.permissions>0)
                r = s.permissions * 2;
            return r(r,links);
        } else if (isDocument(s.url)){
            if (s.atime>0)
                r = s.atime * 4;
            if (s.mtime>0)
                r = s.mtime * 5;
            if (s.group>0)
                r = s.group * 2;
            if (s.owner>0)
                r = s.owner * 2;
            if (s.permissions>0)
                r = s.permissions * 1;
            return r(r,links);
        }
        return r(count,links);
    }

    private Integer r(Integer r, Integer links){
        r = r * links;
        return r;
    }
}
