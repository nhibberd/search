package main.service.index;

import main.data.core.Action;
import main.data.core.Status;
import main.data.file.Documents;
import main.data.index.Names;
import main.data.index.Word;
import main.service.file.FileDb;

import java.sql.Connection;
import java.util.List;

import static main.tool.Database.connector;

public class Indexer {
    private static final String regex = "\\.|\\s|\\,|\\(|\\)|\\[|\\]|\\*|\\?|\\_|\\-";

    /**
     * Indexes files
     */
    public void run() {
        long start = System.currentTimeMillis();

        connector.withConnection(new Action<Connection>() {
            public void apply(final Connection connection) {
                FileDb fileDb = new FileDb();
                List<Names> names = fileDb.getIndexNames(connection);
                for (Names name : names) {
                    add(connection,name);
                    Documents doc = fileDb.get(connection, name.id_file);
                    doc.indexed = true;
                    fileDb.update(connection, doc);
                }
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("\tDone indexing. Run time: " + (end-start));
    }

    private void add(Connection connection, Names name) {
        String[] split = name.name.split(regex);
        check(connection,name.name,1,name.id_file);
        for (String s : split) {
            Integer count = 0;
            if (!s.equals("")){
                for (String word1 : split) {
                    if (s.equals(word1))
                        count+=1;
                }
                check(connection,s,count,name.id_file);
            }

        }
    }

    private void check(Connection connection, String s, Integer count, Integer id_file){
        IndexDb indexDb = new IndexDb();
        String id_file_count = (id_file + ":" + count);

        if (indexDb.exists(connection,s) == Status.OK){
            indexDb.insert(connection,new Word(s,id_file_count));
        } else {
            Word r = new Word(s, indexDb.get(connection,s));
            r.id_file_count = (r.id_file_count + " " + id_file_count);
            indexDb.update(connection,r);
        }
    }

}
