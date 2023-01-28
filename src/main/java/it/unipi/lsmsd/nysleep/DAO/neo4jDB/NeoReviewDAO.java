package it.unipi.lsmsd.nysleep.DAO.neo4jDB;

import it.unipi.lsmsd.nysleep.DAO.ReviewsDAO;
import it.unipi.lsmsd.nysleep.DAO.base.Neo4jBaseDAO;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import it.unipi.lsmsd.nysleep.model.Review;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import static org.neo4j.driver.Values.parameters;

public class NeoReviewDAO extends Neo4jBaseDAO implements ReviewsDAO {

    private Driver driver;

    @Override
    public void createReview(Review review) throws BusinessException {
        driver=initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (cc:customer) WHERE cc.id = $idc" + " MATCH (aa:accommodation) WHERE aa.id = $ida" +
                    " CREATE (cc)-[:REVIEWS {rate: $rate" + ", date: $date}]->(aa)",
                    parameters("idc", review.getCustomer().getId(), "ida", review.getAccommodation().getId(), "rate", review.getRate(), "date", review.getDate()));
        }catch (Exception e){
            throw new BusinessException(e);
        }
        finally {
            close(driver);
        }
    }

    @Override
    public void deleteReview(Review review) throws BusinessException {
        driver=initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (cc:customer { id: $idc"+" })-[r:REVIEWS]->(aa:accommodation { id: $ida"+" }) DELETE r",
                    parameters("idc", review.getCustomer().getId(), "ida", review.getAccommodation().getId()));
        }catch (Exception e){
            throw new BusinessException(e);
        }
        finally {
            close(driver);
        }
    }

    /*public double getAvgRating(Accommodation acc) {
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
    }*/


}