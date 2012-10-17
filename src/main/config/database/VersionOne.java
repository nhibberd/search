package main.config.database;

import main.config.Config;
import main.config.ConfigSetup;
import main.data.core.Function;
import main.data.error.ServerException;
import main.db.Connector;
import main.db.EdgePreparedStatement;
import main.db.EdgeResultSet;
import main.db.Statement;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class VersionOne {
    Config config = new ConfigSetup().stuff();
    private final Connector connector = new Connector("jdbc:hsqldb:file:" + config.dblocation, config.dbuser, config.dbpassword);
    private final Statement statement = new Statement();

    public Boolean setup(){
        final String createuser = "CREATE USER admin PASSWORD agpro";

        final String schema = "CREATE SCHEMA agpro AUTHORIZATION admin\n" +
                "CREATE TABLE IF NOT EXISTS users ( user VARCHAR(255), password VARCHAR(255), growerID INTEGER PRIMARY KEY, salt VARCHAR(255), admin BOOLEAN)\n" +
                "CREATE TABLE IF NOT EXISTS password ( growerID INTEGER, token VARCHAR(255), age BIGINT, sent BOOLEAN)\n" +
                "CREATE TABLE IF NOT EXISTS details (" +
                "ABN VARCHAR(255), GROWERACCOUNT VARCHAR(255), PHYSICALADDRESS VARCHAR(255), TOWN VARCHAR(255), STATE VARCHAR(255), POSTCODE VARCHAR(255), PHONE VARCHAR(255), FAX VARCHAR(255), electronicnotice VARCHAR(255), electronicother VARCHAR(255), PRIMARYCONTACTNAME VARCHAR(255), PRIMARYCONTACTPHONE VARCHAR(255), PRIMARYCONTACTMOBILE VARCHAR(255), PRIMARYCONTACTFAX VARCHAR(255), PRIMARYCONTACTUHF VARCHAR(255), PRIMARYCONTACTEMAIL VARCHAR(255), SPOUSENAME VARCHAR(255), SPOUSECONTACT VARCHAR(255), FARMMANAGER VARCHAR(255), FARMMANAGERCONTACT VARCHAR(255), AGRONOMISTNAME VARCHAR(255), AGROMISTCONTACT VARCHAR(255), MARKETERNAME VARCHAR(255), MARKETERCONTACT VARCHAR(255), ACCOUTNNAME VARCHAR(255), BANKNAME VARCHAR(255), BRANCHACCOUNTHELDAT VARCHAR(255), BSBNUMBER VARCHAR(255), ACCOUNTNUMBER VARCHAR(255), ACCOUNTTYPE VARCHAR(255), BANKCONTACT VARCHAR(255), BANKMOBILE VARCHAR(255), BANKEMAIL VARCHAR(255), BANKPHONE VARCHAR(255), USDACCOUTNNAME VARCHAR(255), USDBANKNAME VARCHAR(255), USDBRANCHACCOUNTHELDAT VARCHAR(255), USDBSBNUMBER VARCHAR(255), USDACCOUNTNUMBER VARCHAR(255), USDACCOUNTTYPE VARCHAR(255), USDBANKCONTACT VARCHAR(255), USDBANKMOBILE VARCHAR(255), USDBANKEMAIL VARCHAR(255), USDBANKPHONE VARCHAR(255), USDSWIFTCODE VARCHAR(255), NAMEA VARCHAR(255), NAMEB VARCHAR(255), NAMEC VARCHAR(255), NAMED VARCHAR(255), NAMEE VARCHAR(255), NAMEF VARCHAR(255), OPERATION VARCHAR(255), AUTHPERSONS VARCHAR(255), AUTHPERSONSOTHER VARCHAR(255), CHECKA BOOLEAN, CHECKB BOOLEAN, CHECKC BOOLEAN, CHECKD BOOLEAN, CHECKLISTACOMPANY VARCHAR(255), CHECKLISTANAME VARCHAR(255), CHECKLISTAMOBILE VARCHAR(255), CHECKLISTAEMAIL VARCHAR(255), CHECKLISTAPHONE VARCHAR(255), CHECKLISTBCOMPANY VARCHAR(255), CHECKLISTBNAME VARCHAR(255), CHECKLISTBMOBILE VARCHAR(255), CHECKLISTBEMAIL VARCHAR(255), CHECKLISTBPHONE VARCHAR(255), AUTH BOOLEAN, NAMEOFAUTHOFFICER VARCHAR(255), DATE BIGINT, GROWERID INTEGER)\n" +
                "CREATE TABLE IF NOT EXISTS question ( TRADINGNAME VARCHAR(255), SHORTNAME VARCHAR(255), REGISTEREDADDRESS VARCHAR(255), PROPERTYCOTTONGROWNON VARCHAR(255), BUSINESSREGISTRATIONNUMBER VARCHAR(255), CONTACTNAME VARCHAR(255), OFFICEPHONE VARCHAR(255), MOBILEPHONE VARCHAR(255), EMAILADDRESS VARCHAR(255), POSITION VARCHAR(255), NATUREOFBUSINESS VARCHAR(255), COMPANYTYPE VARCHAR(255), BELONGTOGROUP VARCHAR(255), KEYSHAREHOLDERS VARCHAR(255), DIRECTORS VARCHAR(255), YEARESTABLISHED VARCHAR(255), YEAROPERATION VARCHAR(255), AUDITEDFINANCIALSTATEMENTS VARCHAR(255), PASTEXPERIENCE VARCHAR(255), TRADEREXPERIENCE VARCHAR(255), INTRODUCER VARCHAR(255), KNOWNCOMPANY VARCHAR(255), RELIABLESOURCE VARCHAR(255), SITEVISIT VARCHAR(255), CONDUCTED VARCHAR(255), DEFAULTED VARCHAR(255), TRADEDCOMMMODITY VARCHAR(255), PLATFORM VARCHAR(255), NAMEOFTRADER VARCHAR(255), BRANCH VARCHAR(255), GROWINGAREA VARCHAR(255), TRADERSCOMMENT VARCHAR(255), COMPETITORTRANSACT VARCHAR(255), YEARSTARTGROWING VARCHAR(255), RAINGROWN VARCHAR(255), IRRIGATED VARCHAR(255), BOTHZ VARCHAR(255), TOTALCOTTONHECTARES VARCHAR(255), TOTALARABLELAND VARCHAR(255), OTHERREMARKS VARCHAR(255), YEARA VARCHAR(255), YEARB VARCHAR(255), YEARC VARCHAR(255), YEARD VARCHAR(255), YEARE VARCHAR(255), GROWERID INTEGER )\n" +
                "CREATE TABLE IF NOT EXISTS mail ( email VARCHAR(255), subject VARCHAR(255), content VARCHAR(2048), id INTEGER GENERATED BY DEFAULT AS IDENTITY )\n" +
                "CREATE TABLE IF NOT EXISTS register ( email VARCHAR(255), growerID INTEGER, uuid VARCHAR(255), sent BOOLEAN, admin BOOLEAN, repID INTEGER, timestamp BIGINT )\n" +
                "CREATE TABLE IF NOT EXISTS adminnotifications ( growerID INTEGER, subject VARCHAR(255), age BIGINT, id INTEGER GENERATED BY DEFAULT AS IDENTITY )\n" +
                "CREATE TABLE IF NOT EXISTS repnotifications ( growerID INTEGER, repID INTEGER, subject VARCHAR(255), age BIGINT, id INTEGER GENERATED BY DEFAULT AS IDENTITY )\n" +
                "CREATE TABLE IF NOT EXISTS notifications ( growerID INTEGER, subject VARCHAR(255), linkto VARCHAR(255), linktourl VARCHAR(255), custom VARCHAR(255), age BIGINT, id INTEGER GENERATED BY DEFAULT AS IDENTITY )\n" +
                "CREATE TABLE IF NOT EXISTS notify ( growerID INTEGER, subject VARCHAR(255), admin BOOLEAN, id INTEGER GENERATED BY DEFAULT AS IDENTITY )\n" +
                "CREATE TABLE IF NOT EXISTS account ( growerID INTEGER, email BOOLEAN)\n" +
                "CREATE TABLE IF NOT EXISTS file ( filepath VARCHAR(255), hash VARCHAR(255), grower INTEGER, timestamp BIGINT, modified BOOLEAN, new BOOLEAN, canconical VARCHAR(255), ordinal VARCHAR(255), mostrecent BOOLEAN )\n" +
                "CREATE TABLE IF NOT EXISTS form ( filepath VARCHAR(255), hash VARCHAR(255), timestamp BIGINT, modified BOOLEAN, new BOOLEAN, canconical VARCHAR(255) )\n" +
                "CREATE TABLE IF NOT EXISTS profile ( grower INTEGER PRIMARY KEY, name VARCHAR(255), phone VARCHAR(255), email VARCHAR(255), notes VARCHAR(1024) )\n" +
                "CREATE TABLE IF NOT EXISTS repclient ( ID INTEGER, grower INTEGER )\n" +
                "CREATE TABLE IF NOT EXISTS repcontact ( repID INTEGER, contactID INTEGER )\n" +
                "CREATE TABLE IF NOT EXISTS contactlist ( email VARCHAR(255) )\n" +
                "CREATE TABLE IF NOT EXISTS blog ( title VARCHAR(255), content VARCHAR(1024), timestamp BIGINT )\n" +
                "CREATE TABLE IF NOT EXISTS schema_version ( version VARCHAR(25) PRIMARY KEY )\n" +
                "GRANT ALL ON users TO admin\n" +
                "GRANT ALL ON password TO admin\n" +
                "GRANT ALL ON details TO admin\n" +
                "GRANT ALL ON question TO admin\n" +
                "GRANT ALL ON mail TO admin\n" +
                "GRANT ALL ON register TO admin\n" +
                "GRANT ALL ON adminnotifications TO admin\n" +
                "GRANT ALL ON repnotifications TO admin\n" +
                "GRANT ALL ON notifications TO admin\n" +
                "GRANT ALL ON notify TO admin\n" +
                "GRANT ALL ON account TO admin\n" +
                "GRANT ALL ON profile TO admin\n" +
                "GRANT ALL ON repclient TO admin\n" +
                "GRANT ALL ON repcontact TO admin\n" +
                "GRANT ALL ON contactlist TO admin\n" +
                "GRANT ALL ON file TO admin\n" +
                "GRANT ALL ON blog TO admin\n" +
                "GRANT ALL ON schema_version TO admin;";

        final String schemaversion = "INSERT INTO main.SCHEMA_VERSION (VERSION) VALUES (1);";
        final String selectversion = "SELECT * FROM main.SCHEMA_VERSION;";


        return connector.withConnection(new Function<Connection, Boolean>() {
            public Boolean apply(final Connection connection) {
                Boolean schemareturn = true;
                Boolean userreturn = true;
                Boolean version = true;
                Boolean selectv = true;
                try{
                    userreturn = create(connection, createuser);
                } catch (ServerException e){
                    userreturn = false;
                }
                try{
                    schemareturn = create(connection, schema);
                } catch (IllegalArgumentException e){
                    schemareturn = false;
                }
                try{
                    selectv = select(connection, selectversion);
                } catch (IllegalArgumentException e){
                    selectv = false;
                }

                if (!selectv){
                    try{
                        version = create(connection, schemaversion);
                    } catch (IllegalArgumentException e){
                        version = false;
                    }
                }
                return true;
            }
        });
    }

    private Boolean create(Connection connection, String sql){
        return statement.withStatement(connection, sql, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                return z.executeUpdate() == 1;
            }
        });
    }

    private Boolean select(Connection connection, String sql){
        return statement.withStatement(connection, sql, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet resultSet = new EdgeResultSet(z);
                return resultSet.next();
            }
        });
    }
}