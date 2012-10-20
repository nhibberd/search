package main.service.query;

import main.data.core.Function;
import main.data.core.Status;
import main.data.index.Id;
import main.service.index.IndexDb;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static main.tool.Database.connector;

public class Index {
    /**
     *
     * @param connection SQL database connection
     * @param word first column of index table
     * @return List of main.data.index.Id
     */
    public static List<Id> getIds(Connection connection, final String word) {
        return connector.withConnection(new Function<Connection, List<Id>>() {
            public List<Id> apply(final Connection connection) {
                IndexDb database = new IndexDb();
                List<Id>  r = new ArrayList<Id>();
                if (database.exists(connection,word) == Status.BAD_REQUEST){
                    String id_file_count = database.get(connection,word);
                    String[] files = id_file_count.split("\\s");
                    for (String file : files) {
                        String[]data = file.split("\\:");
                        r.add(new Id(convert(data[0]), convert(data[1])));
                    }
                }
                return r;
            }
        });


    }

    private static Integer convert(String data){
        return Integer.parseInt( data );
    }

}
