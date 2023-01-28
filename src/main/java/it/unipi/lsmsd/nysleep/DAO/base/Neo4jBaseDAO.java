package it.unipi.lsmsd.nysleep.DAO.base;
import org.neo4j.driver.*;
import static org.neo4j.driver.Values.parameters;

public abstract class Neo4jBaseDAO {
    private static String uri = "bolt://localhost:7687";
    private static String user = "neo4j";
    private static String password = "NY_Sleep";


    public Driver initDriver(Driver driver){
        driver=GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        return driver;
    }

    public void close(Driver driver){
        driver.close();
    }
}