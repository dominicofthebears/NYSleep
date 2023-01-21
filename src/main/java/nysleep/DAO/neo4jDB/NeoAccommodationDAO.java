package nysleep.DAO.neo4jDB;

import nysleep.DAO.AccommodationDAO;
import nysleep.DAO.base.Neo4jBaseDAO;
import nysleep.model.Accommodation;
import nysleep.model.Customer;
import nysleep.model.Renter;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.Entity;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
import org.neo4j.driver.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
            session.run("MATCH (aa: accommodation {id: $oldId"+" }) SET aa.name = $newName"+
                    ", aa.neighborhood = $newNeighborhood"+", aa.rating = $newRating"
                , parameters("oldId", oldAcc.getId(), "newName", newAcc.getName(),
                            "newNeighborhood", newAcc.getNeighborhood(), "newRating", newAcc.getRating()));
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
            session.run("MATCH(cc)-[r:REVIEWS]->(aa:accommodation)<-[o:OWNS]-(rr) WHERE aa.id= $id"+" DELETE o, r"
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
        List<Record> recordList = new ArrayList<>();
        int id2=-1;
        try(Session session = driver.session())
        {
            Result result1 = session.run("CALL gds.graph.drop( 'reviews', false);" +
                            "CALL gds.graph.project( 'reviews', ['customer','accommodation'], ['REVIEWS'])" +
                            "YIELD graphName AS graph, relationshipProjection AS knowsProjection, nodeCount AS nodes, relationshipCount AS rels;" +
                            "CALL gds.nodeSimilarity.stream('reviews')"+
                            "YIELD node1, node2, similarity" +
                            "WHERE gds.util.asNode(node1).id = $id" +
                            "RETURN gds.util.asNode(node1).id AS Customer1, gds.util.asNode(node2).id AS Customer2, similarity" +
                            "ORDER BY similarity DESCENDING, Customer1, Customer2 LIMIT 1"
                    , parameters("id", customer.getId()));
            while(result1.hasNext()) {
                Record record= result1.next();
                id2=record.get("Customer2").asInt();
            }
            Result result2 = session.run("MATCH (cc:customer)-[r:REVIEWS]->(aa:accommodation) WHERE cc.id=$id2 AND r.rate>=4 RETURN aa"
                    , parameters("$id2", id2));
            while(result2.hasNext()) {
                Record record= result2.next();
                recordList.add(record);
            }
            return recordList;
        }finally {
            close(driver);
        }
    }
}
