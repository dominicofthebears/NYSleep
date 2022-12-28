package nysleep.DAO;

import nysleep.model.Review;
import nysleep.model.Customer;
import nysleep.model.Accommodation;


import nysleep.DTO.AccReviewDTO;
import nysleep.DTO.CustomerReviewDTO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.CustomerDTO;


import nysleep.DTO.ReviewDTO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.CustomerDTO;

import java.util.List;


public interface ReviewsDAO{
    void insert(Review review);
    void delete(Review  review);

    PageDTO<AccReviewDTO> getReviewsForAcc(Accommodation acc);
    PageDTO<CustomerReviewDTO> getReviewsForCustomer(Customer customer);
    float getAvgRating(Accommodation acc);

    PageDTO<ReviewAccDTO> viewReviewsForAcc(Accommodation acc);



}
