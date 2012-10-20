package main.schedule.jobs;

import main.data.core.Action;
import main.data.core.Status;
import main.data.file.Documents;
import main.service.file.FileDb;
import main.service.file.LinksDb;

import java.sql.Connection;
import java.util.List;

import static main.tool.Database.connector;

public class Links implements Runnable {
    private Crawler crawler = new Crawler();

    /**
     * Runs as separate thread
     * Checks symlinks to directory's and adds +1 links to any files within those directory's
     *
     */

    public void run() {
        long start = System.currentTimeMillis();
        connector.withConnection(new Action<Connection>() {
            public void apply(final Connection connection) {
                LinksDb linksDb = new LinksDb();
                FileDb fileDb = new FileDb();

                List<main.data.file.Links> listLinks = linksDb.getAll(connection);

                for (main.data.file.Links link : listLinks) {
                    List<Documents> docs = crawler.getDocs(link.dir);

                    for (Documents doc : docs) {
                        if (fileDb.exists(connection,doc.url) == Status.OK)
                            System.out.println("file outside original search param."); //todo
                        else {
                            Documents dbdoc = fileDb.get(connection, doc.url);
                            doc.id = dbdoc.id;
                            doc.hash = dbdoc.hash;
                            doc.links = dbdoc.links + link.links;
                            fileDb.update(connection, doc);
                        }

                    }
                    link.done = true;
                    linksDb.update(connection, link);
                }

            }
        });
        long end = System.currentTimeMillis();
        System.out.println("Done linking. Run time: " + (end-start));
    }

}
