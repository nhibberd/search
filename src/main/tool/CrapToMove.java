package main.tool;

import main.config.Config;
import main.config.ConfigSetup;
import main.db.Connector;
import main.db.Statement;

public class CrapToMove {
    public static final Config config = new ConfigSetup().stuff();


    public static final Connector connector = new Connector("jdbc:hsqldb:file:" + config.dblocation + ";ifexists=true", config.dbuser, config.dbpassword);
    //public static final Connector connector = new Connector("jdbc:postgresql://localhost/testdb", config.dbuser, config.dbpassword);
    public static final Statement statement = new Statement();
}