package main.schedule.jobs;

import main.data.core.Action;
import main.data.core.Status;
import main.data.file.Documents;
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
                IndexDb indexDb = new IndexDb();

                List<Urls> urls = fileDb.getIndexUrls(connection);
                for (Urls url : urls) {

                    String word = url.url.substring(url.url.lastIndexOf("/")+1,url.url.length());
                    String[] words = word.split("\\.|\\s|\\,|\\(|\\)|\\[|\\]|\\*|\\?|\\_|\\-");
                    Integer count = 0;

                    for (String s : words) {
                        System.out.println("s = " + s);
                        if (!s.equals("")){
                            for (String word1 : words) {
                                if (s.equals(word1))
                                    count+=1;
                            }
                            String id_file_count = (url.id_file + ":" + count);

                            if (indexDb.exists(connection,s) == Status.OK){
                                indexDb.insert(connection,new Word(s,id_file_count));
                            } else {
                                Word r = new Word(s, indexDb.get(connection,s));
                                r.id_file_count = (r.id_file_count + " " + id_file_count);
                                indexDb.update(connection,r);
                            }
                            Documents tmp = fileDb.get(connection, url.url);
                            tmp.indexed = true;
                            fileDb.update(connection, tmp);
                        }


                    }



                }
            }
        });
    }
}
