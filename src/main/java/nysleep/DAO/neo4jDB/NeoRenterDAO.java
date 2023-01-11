package nysleep.DAO.neo4jDB;

import nysleep.DAO.UserDAO;
import nysleep.DAO.base.Neo4jBaseDAO;
import nysleep.model.RegisteredUser;
import org.neo4j.driver.Driver;
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
        }
    }

    public void deleteAllRelationship(RegisteredUser user){
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH(rr:renter)-[o:OWNS]->(aa) WHERE rr.id= $id"+" DELETE o"
                    , parameters("id", user.getId()));
        }
    }
}
