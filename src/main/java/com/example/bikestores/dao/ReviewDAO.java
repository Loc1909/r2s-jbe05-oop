package com.example.bikestores.dao;

import com.example.bikestores.entity.Review;
import com.example.bikestores.exception.DAOException;

import java.util.List;

public interface ReviewDAO {
	int submitReview(Review review) throws DAOException;
	List<Review> findByProduct(int productId, Integer minRating, boolean sortNewest) throws DAOException;
	Double getAverageRating(int productId) throws DAOException;
	int getReviewCount(int productId) throws DAOException;
	void updateReviewById(int reviewId, int customerId, Integer rating, String comment) throws DAOException;
	void deleteReviewById(int reviewId, Integer customerId) throws DAOException;
}