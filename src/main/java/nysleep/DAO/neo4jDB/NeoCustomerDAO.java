package nysleep.DAO.neo4jDB;

import nysleep.DAO.UserDAO;
import nysleep.DAO.base.Neo4jBaseDAO;
import nysleep.model.Accommodation;
import nysleep.model.RegisteredUser;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import static org.neo4j.driver.Values.parameters;

public class NeoCustomerDAO extends Neo4jBaseDAO implements UserDAO {
    private Driver driver;

    @Override
    public void register(RegisteredUser user) {
        driver=initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("CREATE (cc: customer {id: $id"+", first_name: $firstName"+", last_name: $lastName"+"})"
                , parameters("id", user.getId(), "firstName", user.getFirstName(), "lastName", user.getLastName()));
        }
    }

    @Override
    public void modifyAccountInfo(RegisteredUser oldUser, RegisteredUser newUser) {
        driver=initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (cc: customer {id: $oldId"+" }) SET cc.first_name = $newFirstName"+
                    ", cc.last_name = $newLastName", parameters("oldId", oldUser.getId(), "newFirstName", newUser.getFirstName(),
                    "newLastName", newUser.getLastName()));
        }
    }

    @Override
    public void deleteAccount(RegisteredUser user) {
        deleteAllRelationship(user);
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (cc: customer {id: $id"+" }) DELETE cc"
                    , parameters("id", user.getId()));
        }
    }

    public void deleteAllRelationship(RegisteredUser user){
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH(cc:customer)-[r:REVIEWS]->(aa) WHERE cc.id= $id"+" DELETE r"
                    , parameters("id", user.getId()));
        }
    }

    public Record mostActiveUser(){
        driver = initDriver(driver);
        Record record=null;
        try(Session session = driver.session())
        {
            Result result=session.run("MATCH (cc:customer)-[r:REVIEWS]->(a:accommodation) " +
                    " RETURN cc.id AS id, cc.first_name AS first_name, cc.last_name AS last_name, COUNT(r) as num_reviews " +
                    "ORDER BY num_reviews DESC LIMIT 1");
            while(result.hasNext()){
                record= result.next();
            }
            return record;
        }
    }
}
