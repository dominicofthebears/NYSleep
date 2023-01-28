package it.unipi.lsmsd.nysleep.DAO;

import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import it.unipi.lsmsd.nysleep.model.Review;


public interface ReviewsDAO{
    void createReview(Review review) throws BusinessException;
    void deleteReview(Review  review) throws BusinessException;

}
