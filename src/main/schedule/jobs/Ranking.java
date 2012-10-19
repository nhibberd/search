package main.schedule.jobs;

import main.data.Ranking.Change;
import main.data.core.Action;
import main.data.core.Status;
import main.data.file.*;
import main.service.file.FileDb;
import main.service.file.LinksDb;
import main.service.file.StateDb;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static main.tool.Database.connector;

public class Ranking implements Runnable {
    private Crawler crawler = new Crawler();

    public void run() {
        connector.withConnection(new Action<Connection>() {
            public void apply(final Connection connection) {
                StateDb database = new StateDb();
                List<Documents> z = crawler.getDocs("/home/nick");
                List<Change> d = new ArrayList<Change>();
                for (Documents documents : z) {
                    d.add(new Change(database.getCount(connection, documents.url), documents.url));
                }
                for (Change data : d) {
                    if (data.count>0){
                        //System.out.println("count:data = " + data.count + ":" +data.dir);
                    }
                }


            }
        });
    }
}
