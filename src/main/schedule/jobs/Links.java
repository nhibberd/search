package main.schedule.jobs;

import main.data.core.Action;
import main.data.file.Documents;
import main.service.file.FileDb;
import main.service.file.LinksDb;

import java.security.PrivateKey;
import java.sql.Connection;
import java.util.List;

import static main.tool.Database.connector;

public class Links implements Runnable {
    private Crawler crawler = new Crawler();
    public void run() {
        connector.withConnection(new Action<Connection>() {
            public void apply(final Connection connection) {
                LinksDb linksDb = new LinksDb();
                FileDb fileDb = new FileDb();

                List<main.data.file.Links> listLinks = linksDb.getAll(connection);

                for (main.data.file.Links link : listLinks) {
                    List<Documents> docs = crawler.getDocs(link.dir);
                    for (Documents doc : docs) {
                        if (doc.hasID()){
                            doc.links += 1;
                            fileDb.update(connection, doc);
                        }

                    }
                    link.done = true;
                    linksDb.update(connection, link);

                }

            }
        });
    }

}
