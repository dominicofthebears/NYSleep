package nysleep.DAO.neo4jDB;

import nysleep.DAO.ReservationDAO;
import nysleep.DAO.ReviewsDAO;
import nysleep.DAO.base.Neo4jBaseDAO;
import nysleep.model.Review;
import nysleep.DTO.AccReviewDTO;
import nysleep.DTO.CustomerReviewDTO;
import nysleep.DTO.PageDTO;
import nysleep.model.Accommodation;
import nysleep.model.Customer;
import nysleep.model.Renter;
import nysleep.DAO.base.MongoBaseDAO;
import org.bson.Document;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

import static org.neo4j.driver.Values.parameters;

public class NeoReviewDAO extends Neo4jBaseDAO implements ReviewsDAO {

    private Driver driver;

    @Override
    public void createReview(Review review) {
        driver=initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (cc:customer) WHERE cc.id = $idc" + " MATCH (aa:accommodation) WHERE aa.id = $ida" +
                    " CREATE (cc)-[:REVIEWS {rate: $rate" + "}]->(aa)",
                    parameters("idc", review.getCustomer().getId(), "ida", review.getAccommodation().getId(), "rate", review.getRate()));
        }finally {
            close(driver);
        }
    }

    @Override
    public void deleteReview(Review review) {
        driver=initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (cc:customer { id: $idc"+" })-[r:REVIEWS]->(aa:accommodation { id: $ida"+" }) DELETE r",
                    parameters("idc", review.getCustomer().getId(), "ida", review.getAccommodation().getId()));
        }finally {
            close(driver);
        }
    }

    public double getAvgRating(Accommodation acc) {
        driver=initDriver(driver);
        try(Session session = driver.session())
        {
            double avgRating = session.readTransaction((TransactionWork<Double>) tx-> {
                Result result = tx.run("MATCH (cc:customer)-[r:REVIEWS]->(aa:accommodation) WHERE aa.id = $id" + " RETURN AVG(r.rate) AS avg_rate", parameters("id", acc.getId()));
                return result.single().get("avg_rate").asDouble();
            });
            return avgRating;
        }finally {
            close(driver);
        }
    }


}