package it.unipi.lsmsd.nysleep.DAO.mongoDB;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import it.unipi.lsmsd.nysleep.DAO.ReviewsDAO;
import it.unipi.lsmsd.nysleep.DAO.base.MongoBaseDAO;
import it.unipi.lsmsd.nysleep.model.Accommodation;
import it.unipi.lsmsd.nysleep.model.RegisteredUser;
import it.unipi.lsmsd.nysleep.model.Review;

import it.unipi.lsmsd.nysleep.model.Customer;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MongoReviewDAO extends MongoBaseDAO implements ReviewsDAO {
    private final String COLLECTION="reviews";
    private static Document toDoc(Review review) {
        //Convert the model object in a document
       Document accommodationDoc = new Document("id",review.getAccommodation().getId())
               .append("name",review.getAccommodation().getName());

       Document customerDoc = new Document("id",review.getCustomer().getId())
               .append("fist_name",review.getCustomer().getFirstName())
               .append("last_name",review.getCustomer().getLastName())
               .append("country",review.getCustomer().getCountry());

       Document doc = new Document("_id",review.getId())
               .append("accommodation",accommodationDoc)
               .append("customer",customerDoc)
               .append("comment",review.getComment())
               .append("rate",review.getRate())
               .append("date",review.getDate());

       return doc;
    }

    public void review() {
    }

    @Override
    public void createReview(Review review) {
        Document doc = toDoc(review);
        insertDoc(doc,COLLECTION);
    }

    public void modifyReview(Review oldReview, Review newReview) {
        Document searchQuery = new Document("_id",new Document("$eq",oldReview.getId()));  //search query
        Document newDoc = toDoc(newReview);  //updated doc
        Document updateQuery = new Document("$set", newDoc); //update query
        updateDoc(searchQuery,updateQuery, COLLECTION);
    }

    @Override
    public void deleteReview(Review review) {
        Document doc = new Document("_id",new Document("$eq",review.getId()));
        deleteDoc(doc,COLLECTION);
    }

    public void deleteAccReview(Accommodation accommdation) {
        Document doc = new Document("accommodation.id",new Document("$eq",accommdation.getId()));
        deleteDoc(doc,COLLECTION);
    }

    public void deleteCustomerReview(Customer customer) {
        Document doc = new Document("customer.id",new Document("$eq",customer.getId()));
        deleteDoc(doc,COLLECTION);
    }

    public List<Document> getReviewsForAcc(Accommodation acc) {
        Document searchQuery = new Document("accommodation.id", new Document("$eq",acc.getId()));
        List<Document> docs = readDocs(searchQuery,COLLECTION);
        return docs;
    }

    public List<Document> getReviewsForCustomer(Customer customer) {
        Document searchQuery = new Document("customer.id", new Document("$eq",customer.getId()));
        List<Document> docs = readDocs(searchQuery,COLLECTION);
        return docs;
    }

    public List<Document> avgRatingByCountry(){
        MongoDatabase db = client.getDatabase(dbName);
        MongoCollection<Document> collection = db.getCollection(COLLECTION);

        String jsonGroupId = "{\"country\":\"$customer.country\"}";
        Document groupId = Document.parse(jsonGroupId);
        AggregateIterable docsIterable = collection.aggregate(
                Arrays.asList(
                        Aggregates.group(groupId, Accumulators.avg("average_rate","$rate"))
                )
        );

        Iterator iterator = docsIterable.iterator();
        List<Document> docs = new ArrayList<Document>();

        while(iterator.hasNext()){
            docs.add((Document) iterator.next());
        }
        return docs;
    }

    public String getCollection(){
        return COLLECTION;
    }

    public boolean checkExistingReview(int customerId, int accommodationId) {
        Document searchQuery = new Document("customer.id", customerId)
                .append("accommodation.id", accommodationId);
        Document doc = readDoc(searchQuery,COLLECTION);
        if(doc == null){
            return false;
        }
        else{
            return true;
        }

    }
}
