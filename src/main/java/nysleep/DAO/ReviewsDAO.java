package nysleep.DAO;

import nysleep.model.Review;
import nysleep.model.Customer;
import nysleep.model.Accommodation;


import nysleep.DTO.AccReviewDTO;
import nysleep.DTO.CustomerReviewDTO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.CustomerReviewDTO;
import nysleep.DTO.PageDTO;

import java.util.List;


public interface ReviewsDAO{
    void createReview(Review review);
    void deleteReview(Review  review);

}
