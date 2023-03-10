package it.unipi.lsmsd.nysleep.DAO.neo4jDB;

import it.unipi.lsmsd.nysleep.DAO.AccommodationDAO;
import it.unipi.lsmsd.nysleep.DAO.base.Neo4jBaseDAO;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import it.unipi.lsmsd.nysleep.model.Accommodation;
import it.unipi.lsmsd.nysleep.model.Customer;
import it.unipi.lsmsd.nysleep.model.Renter;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class NeoAccommodationDAO extends Neo4jBaseDAO implements AccommodationDAO {
    private Driver driver;

    @Override
    public void createAccommodation(Accommodation acc) {
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("CREATE (aa:accommodation {id: $id"+", name: $name"+
                        ", neighborhood: $neighborhood"+", rating: $rating"+", price: $price"+" })"
                , parameters("id", acc.getId(), "name", acc.getName(), "neighborhood", acc.getNeighborhood(), "rating", acc.getRating(), "price", acc.getPrice()));
            linkRenter(acc);
        }finally {
            close(driver);
        }

    }

    @Override
    public void deleteAccommodation(Accommodation acc) {
        deleteAllRelationship(acc);
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (aa: accommodation {id: $id"+" }) DELETE aa"
                , parameters("id", acc.getId()));
        }finally {
            close(driver);
        }
    }

    @Override
    public void updateRating(Accommodation acc, double rating) {
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (aa: accommodation {id: $id"+" }) SET aa.rating = $rating"
                , parameters("id", acc.getId(), "rating", rating));
        }finally {
            close(driver);
        }
    }

    @Override
    public void updateAccommodation(Accommodation oldAcc, Accommodation newAcc) {
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (aa: accommodation {id: $oldId"+" }) SET aa.name = $newName, aa.price = $newPrice"
                , parameters("oldId", oldAcc.getId(), "newName", newAcc.getName(), "newPrice", newAcc.getPrice()));
        }finally {
            close(driver);
        }
    }

    public void linkRenter(Accommodation acc){
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (rr:renter) WHERE rr.id = $idr" + " MATCH (aa:accommodation) WHERE aa.id = $ida" +
                            " CREATE (rr)-[:OWNS]->(aa)",
                    parameters("idr",acc.getRenter().getId(), "ida", acc.getId()));
        }finally {
            close(driver);
        }
    }

    public void deleteAllRelationship(Accommodation acc){
        driver = initDriver(driver);
        try(Session session = driver.session())
        {
            session.run("MATCH (cc:customer)-[r:REVIEWS]->(aa:accommodation)<-[o:OWNS]-(rr:renter) WHERE aa.id= $id"+" DELETE o, r"
                , parameters("id", acc.getId()));

            //running two times, since we may have that the accommodation we are looking for has no reviews
            session.run("MATCH (aa:accommodation)<-[o:OWNS]-(rr:renter) WHERE aa.id= $id"+" DELETE o"
                    , parameters("id", acc.getId()));
        }finally {
            close(driver);
        }
    }

    public List<Record> showRenterAccommodation(Renter renter){
        driver = initDriver(driver);
        List<Record> recordList = new ArrayList<>();
        try(Session session = driver.session())
        {
            Result result = session.run("MATCH(rr:renter)-[o:OWNS]->(aa:accommodation) WHERE rr.id= $id " +
                            "RETURN aa.id AS id, aa.name AS name, aa.neighborhood AS neighborhood, aa.rating AS rating"
                , parameters("id", renter.getId()));
            while(result.hasNext()) {
                Record record= result.next();
                recordList.add(record);
            }
            return recordList;
        }finally {
            close(driver);
        }
    }

    public List<Record> showSuggestedAccommodation(Customer customer){
        driver = initDriver(driver);
        List<Record> recordList = new LinkedList<>();
        int id2=-1;
        try(Session session = driver.session())
        {
            //deletes the projection, if it already exists
            session.run("CALL gds.graph.drop( 'reviews', false);");
            //creates a new projection
            session.run("CALL gds.graph.project( 'reviews', ['customer','accommodation'], ['REVIEWS'])" +
                    "YIELD graphName AS graph, relationshipProjection AS knowsProjection, nodeCount AS nodes, relationshipCount AS rels;");
            //calculates the most similar customer
            Result result1 = session.run("CALL gds.nodeSimilarity.stream('reviews') "+
                            "YIELD node1, node2, similarity " +
                            "WHERE gds.util.asNode(node1).id = $id " +
                            "RETURN gds.util.asNode(node1).id AS Customer1, gds.util.asNode(node2).id AS Customer2, similarity " +
                            "ORDER BY similarity DESCENDING, Customer1, Customer2 LIMIT 1"
                    , parameters("id", customer.getId()));
            while(result1.hasNext()) {
                Record record= result1.next();
                id2=record.get("Customer2").asInt();
            }
            //retrieves the accommodation that the similar customer had rated well (4 or more)
            Result result2 = session.run("MATCH (cc:customer)-[r:REVIEWS]->(aa:accommodation) WHERE cc.id=$id2 AND r.rate>=4 "+
                            "RETURN aa.id AS id, aa.name AS name, aa.neighborhood as neighborhood, aa.rating as rating, aa.price as price"
                    , parameters("id2", id2));
            while(result2.hasNext()) {
                Record record= result2.next();
                recordList.add(record);
            }
            return recordList;
        }finally {
            close(driver);
        }
    }

    public double recomputeRate(Accommodation acc) throws BusinessException {
        driver = initDriver(driver);
        double res;
        LocalDate date = LocalDate.now();
        int year = date.getYear() - 1;
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (aa:accommodation)<-[r:REVIEWS]-() " +
                            "WHERE aa.id=$id AND r.date>date({year: $year, month: $month, day: $day}) RETURN AVG(r.rate) AS rate"
                    , parameters("id", acc.getId(), "year", year, "month", month, "day", day));
            Value v = result.single().get("rate");
            if(!v.isNull())
                res = v.asDouble();
            else
               res = Double.NaN;
            return res;
        }catch (Exception e){
            throw new BusinessException(e);
        }
        finally{
            close(driver);
        }
    }

    public List<Record> showAccommodationOfLikedRenter(Customer customer){
        driver = initDriver(driver);
        List<Record> recordList = new LinkedList<>();
        try(Session session = driver.session()){
            Result result = session.run("MATCH (cc:customer)-[r:REVIEWS]->(aa:accommodation)<-[o:OWNS]-(rr:renter)-[so:OWNS]->(sa:accommodation) "+"" +
                    "WHERE r.rate>3 AND cc.id=$id  RETURN sa.id AS id, sa.name AS name, sa.neighborhood as neighborhood, sa.rating as rating, sa.price as price"
                    , parameters("id", customer.getId()));
            while(result.hasNext()) {
                Record record= result.next();
                recordList.add(record);
            }
            return recordList;
        }finally {
            close(driver);
        }
    }
}
