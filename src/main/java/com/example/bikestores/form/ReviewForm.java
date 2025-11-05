package com.example.bikestores.form;

import com.example.bikestores.entity.Review;
import com.example.bikestores.util.Constants;
import com.example.bikestores.util.ScannerUtil;
import com.example.bikestores.util.ValidationUtil;

import java.time.LocalDate;

public class ReviewForm {

	public static Review inputNewReview() {
		int customerId = ScannerUtil.readInt("Enter customer ID: ");
		int productId = ScannerUtil.readInt("Enter product ID: ");
		int rating = ValidationUtil.readRating(
			"Enter rating (" + Constants.RATING_MIN + "-" + Constants.RATING_MAX + "): ",
			Constants.RATING_MIN,
			Constants.RATING_MAX
		);

		String comment = ValidationUtil.readComment(
			"Enter comment (optional, type 'null' for empty, max " + Constants.COMMENT_MAX_LENGTH + " chars): ",
			Constants.COMMENT_MAX_LENGTH
		);

		Review r = new Review();
		r.setCustomerId(customerId);
		r.setProductId(productId);
		r.setRating(rating);
		r.setComment(comment);
		r.setReviewDate(LocalDate.now());
		return r;
	}

	public static int inputProductId() {
        return ScannerUtil.readInt("Enter product ID: ");
	}

	public static Integer inputMinRatingOrNull() {
		int value = ScannerUtil.readInt("Enter min rating (" + Constants.RATING_MIN + "-" + Constants.RATING_MAX + ", 0 to skip): ");
		if (value == 0)
            return null;
		return Math.max(Constants.RATING_MIN, Math.min(Constants.RATING_MAX, value));
	}

	public static boolean inputSortNewest() {
		int s = ScannerUtil.readInt("Sort by newest? (1=yes, 0=no): ");
		return s == 1;
	}

	public static int inputReviewId(String action) {
        return ScannerUtil.readInt("Enter review ID to " + action + ": ");
	}

	public static int inputCustomerId() {
        return ScannerUtil.readInt("Enter customer ID: ");
	}

	public static int inputNewRating() {
		return ValidationUtil.readRating(
			"Enter new rating (" + Constants.RATING_MIN + "-" + Constants.RATING_MAX + "): ",
			Constants.RATING_MIN,
			Constants.RATING_MAX
		);
	}

	public static String inputNewComment() {
		return ValidationUtil.readComment(
			"Enter new comment (optional, 'null' to clear, max " + Constants.COMMENT_MAX_LENGTH + " chars): ",
			Constants.COMMENT_MAX_LENGTH
		);
	}
}