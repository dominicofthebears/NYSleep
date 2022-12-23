package nysleep.DAO;

import nysleep.model.Review;
import nysleep.model.Customer;
import nysleep.model.Accommodation;

import nysleep.DTO.AccReviewDTO;
import nysleep.DTO.CustomerReviewDTO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.CustomerDTO;


public interface ReviewsDAO{
    void insert(Review review);
    void delete(Review  review);
    PageDTO<AccReviewDTO> getReviewsForAcc(Accommodation acc);
    PageDTO<CustomerReviewDTO> getReviewsForCustomer(Customer customer);
    float getAvgRating(Accommodation acc);
}
