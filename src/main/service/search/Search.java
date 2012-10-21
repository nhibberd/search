package main.service.search;

import main.config.ConfigSetup;
import main.config.Schema;
import main.config.database.VersionOne;
import main.data.core.Action;
import main.data.core.Function;
import main.data.core.Result;
import main.data.index.Id;
import main.data.rank.Score;
import main.schedule.background.Threads;
import main.schedule.jobs.Crawler;
import main.schedule.jobs.Links;
import main.service.query.Applications;
import main.service.query.Documents;
import main.service.query.Files;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static main.data.state.Params.records;
import static main.service.query.Index.getIds;
import static main.service.rank.RankFunctions.isApplication;
import static main.tool.Database.connector;

public class Search {
    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;


    /**
     * Simple command line interface to search for terms
     *
     *
     * search term    -- search for any files matching term, return top result
     * search -x term -- search for any files matching term that are executable
     * search -d term -- search for any document matching term
     * search -n 1 term -- search for any files matching term, but only return the top results
     * search -n 5 term -- search for any files matching term, but only return the top 5 results
     * search -xn 5 term -- search for any executables matching term, but only return the top 5 results
     * search -dn 5 term -- search for any documents matching term, but only return the top 5 results
     *
     * `term` -- single "word" or multiply words "id rsa" separated by white space
     *
     * @param args Search options and terms
     */
    public static void main(final String[] args){
        final Files filesQuery = new Files();
        final Applications appQuery = new Applications();
        final Documents docQuery = new Documents();

        //db setup
        new VersionOne().setup();
        new Schema().apply();

        //program arguments
        ConfigSetup.set(new File("config.properties"));
        if (args.length > 0){
            connector.withConnection(new Action<Connection>() {
                public void apply(final Connection connection) {
                    String first = args[0];

                    //option
                    if (first.startsWith("-")){
                        if (first.length()==2) {
                            if (first.equals("-x")){
                                String r = appQuery.top(connection,args[1]);
                                System.out.println(args[1] + " : " + r);
                            } else if (first.equals("-d")){
                                String r = docQuery.top(connection,args[1]);
                                System.out.println(args[1] + " : " + r);
                            }   else if (first.equals("-n")){
                                List<String> r = filesQuery.list(connection,args[2], Integer.parseInt(args[1]));
                                System.out.println(args[2] + " : " + r.toString());
                            }
                        } else if (first.length()==3) {
                            if (first.equals("-xn")){
                                List<String>  r = appQuery.list(connection, args[2], Integer.parseInt(args[1]));
                                System.out.println(args[2] + " : " + r.toString());
                            } else if (first.equals("-dn")){
                                List<String>  r = docQuery.list(connection,args[2], Integer.parseInt(args[1]));
                                System.out.println(args[2] + " : " + r.toString());
                            }
                        }
                    } else {
                        String r = filesQuery.top(connection, args[0]);
                        System.out.println(args[0] + " : " + r);

                    }
                }
            });
        }
    }
}
