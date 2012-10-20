package main.service.query;

import main.service.file.FileDb;
import main.service.file.StateDb;
import main.service.index.IndexDb;
import main.service.rank.RankDb;

public class Select {
    private StateDb stateDb = new StateDb();
    private FileDb fileDb = new FileDb();
    private IndexDb indexDb = new IndexDb();
    private RankDb rankDb = new RankDb();

    private Applications apps = new Applications();
    private Documents docs = new Documents();



    /*
    todo
    Single: check for search term. getScore documents attached, getScore rank of documents, produce ranked list

    Multi: iterate(check term, getScore documents, getScore rank, add to list ), produce ranked list from all terms.
            ||   only getScore documents which have all terms?


     */
}
