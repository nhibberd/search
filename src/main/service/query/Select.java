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

import static main.service.query.Index.getIds;
import static main.tool.Database.connector;

public class Select {
    private StateDb stateDb = new StateDb();
    private FileDb fileDb = new FileDb();
    private IndexDb indexDb = new IndexDb();
    private RankDb rankDb = new RankDb();

    private SelectApplications apps = new SelectApplications();
    private SelectDocuments docs = new SelectDocuments();



    /*
    todo
    Single: check for search term. getScore documents attached, getScore rank of documents, produce ranked list

    Multi: iterate(check term, getScore documents, getScore rank, add to list ), produce ranked list from all terms.
            ||   only getScore documents which have all terms?


     */
}
