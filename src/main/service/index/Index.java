package main.service.index;

import main.data.core.Function;
import main.data.index.Id;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static main.tool.Database.connector;

public class Index {
    //todo make index publicly available, take off thread.

    public List<Id> get(Connection connection, final String word) {
        return connector.withConnection(new Function<Connection, List<Id>>() {
            public List<Id> apply(final Connection connection) {
                IndexDb database = new IndexDb();

                List<Id>  r = new ArrayList<Id>();
                String id_file_count = database.get(connection,word);
                String[] files = id_file_count.split("\\s");
                for (String file : files) {
                    String[]data = file.split("\\:");
                    r.add(new Id(convert(data[0]), convert(data[1])));
                }
                return r;
            }
        });


    }

    private Integer convert(String data){
        return Integer.parseInt( data );
    }

}
