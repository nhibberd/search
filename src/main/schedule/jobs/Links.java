package main.schedule.jobs;

import main.data.core.Action;
import main.service.file.LinksDb;

import java.sql.Connection;

import static main.tool.Database.connector;

public class Links {

    //todo calculate how many of links to files
    //then add to FileFunctions table, links INTEGER


    // get all

    // check if is directory

    // if so, then iterator through directory and add link to any file there, then check done to true
    public void run() {
        connector.withConnection(new Action<Connection>() {
            public void apply(final Connection connection) {
                update(connection, new main.data.file.Links("dir",0,false));
            }
        });
    }

    public void update(Connection connection, main.data.file.Links input){
        LinksDb database = new LinksDb();
        database.update(connection, input);
    }

}
