package nysleep.DAO.neo4jDB;

import nysleep.DAO.AccommodationDAO;
import nysleep.DAO.base.Neo4jBaseDAO;
import nysleep.model.Accommodation;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;

public class NeoAccommodationDAO extends Neo4jBaseDAO implements AccommodationDAO {
    private Driver driver;

    @Override
    public void createAccommodation(Accommodation acc) {
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("CREATE (aa:accommodation {id: $id"+", name: $name"+
                        ", neighborhood: $neighborhood"+", rating: $rating"+" })"
                , parameters("id", acc.getId(), "name", acc.getName(), "neighborhood", acc.getNeighborhood(), "rating", acc.getRating()));
        }
    }

    @Override
    public void deleteAccommodation(Accommodation acc) {
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (aa: accommodation {id: $id"+" }) DELETE aa"
                , parameters("id", acc.getId()));
        }
    }

    @Override
    public void updateRating(Accommodation acc, double rating) {
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (aa: accommodation {id: $id"+" }) SET aa.rating = $rating"
                , parameters("id", acc.getId(), "rating", rating));
        }
    }

    @Override
    public void updateAccommodation(Accommodation oldACc, Accommodation newAcc) {
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (aa: accommodation {id: $oldId"+" }) SET aa.name = $newName"+
                    ", aa.neighborhood = $newNeighborhood"+", aa.rating = $newRating"
                , parameters("oldId", oldACc.getId(), "newName", newAcc.getName(),
                            "newNeighborhood", newAcc.getNeighborhood(), "newRating", newAcc.getRating()));
        }
    }
}
