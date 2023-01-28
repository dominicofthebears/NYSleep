package it.unipi.lsmsd.nysleep.DAO.neo4jDB;

import it.unipi.lsmsd.nysleep.DAO.UserDAO;
import it.unipi.lsmsd.nysleep.DAO.base.Neo4jBaseDAO;
import it.unipi.lsmsd.nysleep.model.RegisteredUser;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import static org.neo4j.driver.Values.parameters;

public class NeoRenterDAO extends Neo4jBaseDAO implements UserDAO {
    private Driver driver;

    @Override
    public void register(RegisteredUser user) {
        driver=initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("CREATE (rr: renter {id: $id"+", first_name: $firstName"+", last_name: $lastName"+"})"
                    , parameters("id", user.getId(), "firstName", user.getFirstName(), "lastName", user.getLastName()));
        }finally {
            close(driver);
        }
    }

    @Override
    public void modifyAccountInfo(RegisteredUser oldUser, RegisteredUser newUser) {
        driver=initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (rr: renter {id: $oldId"+" }) SET rr.first_name = $newFirstName"+
                    ", rr.last_name = $newLastName", parameters("oldId", oldUser.getId(), "newFirstName", newUser.getFirstName(),
                    "newLastName", newUser.getLastName()));
        }finally {
            close(driver);
        }
    }

    @Override
    public void deleteAccount(RegisteredUser user) {
        deleteAllRelationship(user);
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (rr: renter {id: $id"+" }) DELETE rr"
                    , parameters("id", user.getId()));
        }finally {
            close(driver);
        }
    }

    public void deleteAllRelationship(RegisteredUser user){
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH(rr:renter)-[o:OWNS]->(aa) WHERE rr.id= $id"+" DELETE o"
                    , parameters("id", user.getId()));
        }finally {
            close(driver);
        }
    }

    public Record renterWithMostAccommodation(){
        driver = initDriver(driver);
        Record record=null;
        try(Session session = driver.session())
        {
            Result result = session.run("MATCH (rr:renter)-[o:OWNS]->(a:accommodation) " +
                    " RETURN rr.id AS id, rr.first_name AS first_name, rr.last_name AS last_name, " +
                    "COUNT(o) as num_accommodations ORDER BY num_accommodations DESC LIMIT 1");
            while(result.hasNext()){
                record= result.next();
            }
            return record;
        }finally {
            close(driver);
        }
    }

    public Record bestReviewedRenter(int minReviews){
        driver = initDriver(driver);
        Record record=null;
        try(Session session = driver.session())
        {
            Result result = session.run("MATCH (cc:customer)-[r:REVIEWS]->(aa:accommodation)<-[o:OWNS]-(rr:renter) " +
                    "WITH COUNT(r) as num_reviews, rr, AVG(r.rate) as avg_rate  WHERE num_reviews >= $lim " +
                    "RETURN rr.id AS id, rr.first_name AS first_name, rr.last_name AS last_name, avg_rate " +
                    "ORDER BY avg_rate DESC LIMIT 1", parameters("lim", minReviews));
            while(result.hasNext()){
                record= result.next();
            }
            return record;
        }finally {
            close(driver);
        }
    }

    public Record renterWithMostAccommodationForNeighborhood(String neighborhood){
        driver = initDriver(driver);
        Record record=null;
        try(Session session = driver.session())
        {
            Result result = session.run("MATCH (rr:renter)-[o:OWNS]->(aa:accommodation) WHERE aa.neighborhood= $neighborhood " +
                    "RETURN rr.id AS id, rr.first_name AS first_name, rr.last_name AS last_name, COUNT(o) as num_accommodations_neighborhood " +
                            "ORDER BY num_accommodations_neighborhood DESC LIMIT 1"
                    , parameters("neighborhood", neighborhood));
            while(result.hasNext()){
                record= result.next();
            }
            return record;
        }finally {
            close(driver);
        }
    }
}
