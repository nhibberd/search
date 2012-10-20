package main.schedule.jobs;

import main.data.core.Action;
import main.data.core.Status;
import main.data.index.Urls;
import main.data.index.Word;
import main.service.file.FileDb;
import main.service.file.LinksDb;
import main.service.file.StateDb;
import main.service.index.IndexDb;

import java.sql.Connection;
import java.util.List;

import static main.tool.Database.connector;

public class Indexing implements Runnable  {



    public void run() {
        connector.withConnection(new Action<Connection>() {
            public void apply(final Connection connection) {
                FileDb fileDb = new FileDb();
                LinksDb linksDb = new LinksDb();
                StateDb stateDb = new StateDb();
                IndexDb indexDb = new IndexDb();

                List<Urls> urls = fileDb.getIndexUrls(connection);
                for (Urls url : urls) {
                    String[] words = url.url.split("/");
                    Integer count = 0;
                    for (String word : words) {
                        count = 1;
                        for (String s : words) {
                            if (word.equals(s))
                                count+=1;
                        }
                        String id_file_count = (url.id_file + ":" + count);

                        if (indexDb.exists(connection,word) == Status.OK){
                            indexDb.insert(connection,new Word(word,id_file_count));
                        } else {

                            Word r = new Word(word, indexDb.get(connection,word));
                            r.id_file_count = (r.id_file_count + ", " + id_file_count);
                            indexDb.update(connection,r);

                        }

                        //todo for each word, check if in db ? add file id : add to db + file id ;
                    }
                    //System.out.println("url: " + url.id_file + " : " + url.url);

                    /* todo
                   # INDEX DB ( id_file, words, )?
                   break up urls by "/"
                   insert words into db, with id_file

                    */

                    //todo finally update filesDb, set indexed = true;


                }



            }
        });
    }
}
