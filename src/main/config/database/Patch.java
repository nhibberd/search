package main.config.database;

import main.config.Config;
import main.config.ConfigSetup;
import main.db.Connector;

public class Patch {
    Config config = new ConfigSetup().stuff();
    public final Connector connector = new Connector("jdbc:hsqldb:file:" + config.dblocation + ";ifexists=true", config.dbuser, config.dbpassword);
}
