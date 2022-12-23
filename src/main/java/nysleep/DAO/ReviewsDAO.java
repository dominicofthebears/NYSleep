package nysleep.DAO;

import nysleep.model.Review;
import nysleep.model.Customer;
import nysleep.model.Accommodation;

import nysleep.DTO.ReviewDTO;
import nysleep.DTO.PageDTO;
import nysleep.DTO.CustomerDTO;

import java.util.List;

public interface ReviewsDAO{
    void insert(Review review);
    void delete(Review  review);
    PageDTO<ReviewAccDTO> viewReviewsForAcc(Accommodation acc);


}
