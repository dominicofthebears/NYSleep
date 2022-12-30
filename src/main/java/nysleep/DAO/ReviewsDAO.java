package nysleep.DAO;

import nysleep.model.Review;
import nysleep.model.Customer;
import nysleep.model.Accommodation;


import nysleep.DTO.ReviewAccDTO;
import nysleep.DTO.CustomerReviewDTO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.CustomerReviewDTO;
import nysleep.DTO.PageDTO;

import java.util.List;


public interface ReviewsDAO{
    void insert(Review review);
    void delete(Review  review);

    PageDTO<ReviewAccDTO> getReviewsForAcc(Accommodation acc);
    PageDTO<CustomerReviewDTO> getReviewsForCustomer(Customer customer);
    float getAvgRating(Accommodation acc);

    PageDTO<ReviewAccDTO> viewReviewsForAcc(Accommodation acc);



}
