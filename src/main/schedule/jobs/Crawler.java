package main.schedule.jobs;

import main.data.core.Action;
import main.data.core.Status;
import main.data.error.ServerException;
import main.data.file.AllFiles;
import main.data.file.Documents;
import main.data.file.Links;
import main.data.file.State;
import main.service.file.FileDb;
import main.service.file.LinksDb;
import main.service.file.StateDb;
import main.service.index.Indexer;
import main.service.rank.Ranker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static main.data.state.Params.records;
import static main.service.file.FileFunctions.addFile;
import static main.service.file.FileFunctions.hash;
import static main.tool.Database.connector;

public class Crawler implements Runnable {
    private Indexer index = new Indexer();
    private Ranker rank = new Ranker();

    public void run() {
        System.out.println("Crawling...");
        long start = System.currentTimeMillis();
        final AllFiles files = list(new File(records.get("dir")));

        connector.withConnection(new Action<Connection>() {
            public void apply(final Connection connection) {
                eval(connection, files.docs);
                evalLinks(connection, files.links);
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("done crawling. Run time: " + (end - start));
        index.run();
        rank.run();

    }

    public List<Documents> getDocs(String dir) {
        return list(new File(dir)).docs;
    }


    private AllFiles list(File f) {
        return list(f, new AllFiles(new ArrayList<Documents>(), new ArrayList<Path>()));
    }

    private AllFiles list(File f, AllFiles acc) {
        Path file = null;
        if (!f.canRead())
            return acc;
        else if (f.canRead())
            file = Paths.get(f.getAbsolutePath());
        if ((f.isFile() && !Files.isSymbolicLink(file)) || (!f.isDirectory() && !Files.isSymbolicLink(file)))  {
            return acc.adddoc(addFile(file));
        }
        else if (Files.isSymbolicLink(file)){
            return acc.addlink(file);
        } else {
            AllFiles r = acc;
            for (File q : f.listFiles())
                r = r.add(list(q, acc));
            return r;
        }
    }


    private void eval(Connection connection, List<Documents> docs) {
        FileDb fileDb = new FileDb();
        StateDb stateDb = new StateDb();
        for (Documents doc : docs) {

            if (fileDb.exists(connection,doc.url) == Status.OK){
                doc.hash = hash(doc.url);
                doc.indexed = false;
                fileDb.insert(connection, doc);

                if (stateDb.exists(connection, doc.url) == Status.OK)
                    stateDb.insert(connection,new State(doc.url));
            } else {
                Documents dbdoc = fileDb.get(connection, doc.url);

                //works
                if (!doc.compare(dbdoc)){
                    //Don't equal each other
                    State state = stateDb.get(connection, dbdoc.url);

                    doc.hash = hash(doc.url);

                    if (!dbdoc.times.compare("atime",doc.times.atime)) {
                        dbdoc.times.atime = doc.times.atime;
                        state.atime+=1;
                    }
                    if (!dbdoc.times.compare("mtime",doc.times.mtime)) {
                        dbdoc.times.mtime = doc.times.mtime;
                        state.mtime+=1;
                    }
                    if (!dbdoc.times.compare("ctime",doc.times.ctime))
                        dbdoc.times.ctime = doc.times.ctime;
                    if (!dbdoc.compareHidden(doc.hidden))
                        dbdoc.hidden = doc.hidden;
                    if (!dbdoc.compareOther(doc.other))
                        dbdoc.other = doc.other;
                    if (!dbdoc.compareRegfile(doc.regfile))
                        dbdoc.regfile = doc.regfile;
                    if (!dbdoc.compareGroup(doc.group)) {
                        dbdoc.group = doc.group;
                        state.group+=1;
                    }
                    if (!dbdoc.compareOwner(doc.owner)) {
                        dbdoc.owner = doc.owner;
                        state.owner+=1;
                    }
                    if (!dbdoc.comparePermissions(doc.permissions)){
                        dbdoc.permissions =doc.permissions;
                        state.permissions+=1;
                    }
                    if (!dbdoc.compareHash(doc.hash))
                        dbdoc.hash = doc.hash;
                    fileDb.update(connection, dbdoc);
                    stateDb.update(connection,state);
                }
            }
        }
    }

    private void evalLinks(Connection connection, List<Path> links){
        LinksDb linksDb = new LinksDb();

        FileDb fileDb = new FileDb();
        for (Path link : links) {
            String dir;
            try {
                dir = link.toFile().getCanonicalPath();
            } catch (IOException e) {
                throw new ServerException(e);
            }
            if (linksDb.exists(connection,dir) == Status.OK){
                File d = new File(dir);
                if (d.isFile()){
                    if (fileDb.exists(connection,dir) == Status.OK){
                        Documents data = fileDb.get(connection,dir);
                        data.links =+ 1;
                        fileDb.update(connection,data);
                    }
                }
                linksDb.insert(connection, new Links(dir, 1, false));
            } else {
                Links data = linksDb.get(connection, dir);
                if (!data.done) {
                    data.links+=1;
                    linksDb.update(connection, data);
                }
            }
        }
    }
}
