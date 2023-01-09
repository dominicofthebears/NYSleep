package nysleep.DAO.neo4j;

import nysleep.DAO.ReviewsDAO;
import nysleep.DAO.base.Neo4jBaseDAO;
import nysleep.DTO.ReviewAccDTO;
import nysleep.DTO.CustomerReviewDTO;
import nysleep.DTO.PageDTO;
import nysleep.model.Accommodation;
import nysleep.model.Customer;
import nysleep.model.Renter;
import nysleep.DAO.base.MongoBaseDAO;
import nysleep.model.Review;
import org.bson.Document;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;

public class Neo4jReviewsDAO extends Neo4jBaseDAO implements ReviewsDAO{
    private Driver driver;

    @Override
    public Driver initDriver(Driver driver) {
        return super.initDriver(driver);
    }


    @Override
    public void insert(Review review) {

    }

    @Override
    public void delete(Review review) {

    }

    @Override
    public double getAvgRating(Accommodation acc) {
        driver=initDriver(driver);
        try(Session session = driver.session())
        {
            double avgRating = session.readTransaction((TransactionWork<Double>) tx-> {
               Result result = tx.run("MATCH (cc:customer)-[r:REVIEWS]->(aa:accommodation) WHERE aa.id = $id" + " RETURN AVG(r.rate) AS avg_rate", parameters("id", acc.getId()));
               return result.single().get("avg_rate").asDouble();
            });
            return avgRating;
        }
    }
}
