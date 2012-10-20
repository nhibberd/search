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

    /**
     * Ranking algorithm
     *
     * Files:
     *  Properties : weight
     *  Accessed : 5
     *  Modified : 5
     *  Group : 2
     *  Owner : 2
     *  Permissions : 1
     *
     * Applications:
     *  Properties : weight
     *  Accessed : 5
     *  Modified : 1
     *  Group : 2
     *  Owner : 2
     *  Permissions : 2
     *
     */

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


    /**
     * Score based from number of changes and weighting according to
     * ranking algorithm
     *
     * @param s State
     * @param links Number of links to file
     * @param count Number of changes on file ( acccess, modification, group, owner, permissions )
     * @return Integer - score
     */
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
