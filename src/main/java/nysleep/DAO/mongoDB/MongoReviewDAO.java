package nysleep.DAO.mongoDB;

import nysleep.DAO.ReviewsDAO;
import nysleep.DAO.base.MongoBaseDAO;

import nysleep.DTO.CustomerReviewDTO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.AccReviewDTO;

import nysleep.model.Accommodation;
import nysleep.model.Customer;

import nysleep.model.Review;
import org.bson.Document;

import java.util.ArrayList;

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

       Document doc = new Document("id",review.getId())
               .append("accommodation",accommodationDoc)
               .append("customer",customerDoc)
               .append("comment",review.getComment())
               .append("rate",review.getRate())
               .append("date",review.getDate());

       return doc;
    }

    @Override
    public void createReview(Review review) {
        Document doc = toDoc(review);
        insertDoc(doc,COLLECTION);
    }

    @Override
    public void deleteReview(Review review) {
        Document doc = toDoc(review);
        deleteDoc(doc,COLLECTION);
    }

    public PageDTO<AccReviewDTO> getReviewsForAcc(Accommodation acc) {
        Document searchQuery = new Document("accommodation.id", new Document("$eq",acc.getId()));
        ArrayList<Document> docs = readDoc(searchQuery,COLLECTION);
        ArrayList<AccReviewDTO> AccReviewDTOList = new ArrayList<AccReviewDTO>();

        for(Document doc: docs){

            Document customerDoc = (Document) doc.get("customer");

            AccReviewDTO accReviewDTO = new AccReviewDTO(
                    (int) customerDoc.get("id")
                    ,(String)customerDoc.get("first_name")
                    ,(String)customerDoc.get("last_name")
                    ,(String)customerDoc.get("country")
                    ,(int) doc.get("rate")
                    ,(String)doc.get("comment"));

            AccReviewDTOList.add(accReviewDTO);
        }

        PageDTO<AccReviewDTO> pageDTO = new PageDTO<AccReviewDTO>();
        pageDTO.setEntries(AccReviewDTOList);
        return pageDTO;
    }


    public PageDTO<CustomerReviewDTO> getReviewsForCustomer(Customer customer) {
        Document searchQuery = new Document("customer.id", new Document("$eq",customer.getId()));
        ArrayList<Document> docs = readDoc(searchQuery,COLLECTION);
        ArrayList<CustomerReviewDTO> customerReviewDTOList = new ArrayList<CustomerReviewDTO>();

        for(Document doc: docs){

            Document accommodationDoc = (Document) doc.get("accommodation");

            CustomerReviewDTO customerReviewDTO = new CustomerReviewDTO(
                    (int) accommodationDoc.get("id")
                    ,(String)accommodationDoc.get("name")
                    ,(int) doc.get("rate")
                    ,(String) doc.get("comment")
            );

            customerReviewDTOList.add(customerReviewDTO);
        }

        PageDTO<CustomerReviewDTO> pageDTO = new PageDTO<CustomerReviewDTO>();
        pageDTO.setEntries(customerReviewDTOList);
        return pageDTO;
    }

}
