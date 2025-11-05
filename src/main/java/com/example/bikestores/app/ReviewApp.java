package com.example.bikestores.app;

import com.example.bikestores.dao.ReviewDAO;
import com.example.bikestores.dao.ReviewDAOImpl;
import com.example.bikestores.entity.Review;
import com.example.bikestores.exception.DAOException;
import com.example.bikestores.exception.GlobalExceptionHandler;
import com.example.bikestores.form.ReviewForm;
import com.example.bikestores.util.Constants;
import com.example.bikestores.util.StringUtil;
import com.example.bikestores.auth.UserSession;

import java.util.List;
import java.util.Scanner;

public class ReviewApp {


	public static void run() {
		ReviewDAO reviewDAO = new ReviewDAOImpl();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\n===== REVIEW MENU =====");
            System.out.println(Constants.REVIEW_MENU_LIST + ". List reviews by product");
            System.out.println(Constants.REVIEW_MENU_SUBMIT + ". Submit a review");
            System.out.println(Constants.REVIEW_MENU_UPDATE + ". Update own review");
            System.out.println(Constants.REVIEW_MENU_DELETE + ". Delete review");
            System.out.println(Constants.MENU_BACK + ". Back to previous menu");
			System.out.print("Choose: ");
			String choiceInput = scanner.nextLine();

			int choice;
			try {
				choice = Integer.parseInt(choiceInput);
			} catch (NumberFormatException e) {
				System.out.println("Invalid choice. Please enter a number.");
				continue;
			}

			try {
                switch (choice) {
                    case Constants.REVIEW_MENU_LIST -> {
						int productId = ReviewForm.inputProductId();
						Integer minRating = ReviewForm.inputMinRatingOrNull();
						boolean sortNewest = ReviewForm.inputSortNewest();

						List<Review> list = reviewDAO.findByProduct(productId, minRating, sortNewest);
						if (list.isEmpty()) {
							System.out.println("No reviews found for this product.");
						} else {
							System.out.println(Constants.REVIEW_HEADER);
							for (Review r : list) {
								System.out.printf(Constants.REVIEW_ROW_FORMAT + "%n",
										r.getReviewId(), r.getCustomerId(), r.getProductId(), r.getRating(), StringUtil.truncate(r.getComment(), 40), r.getReviewDate());
							}
						}
						Double avg = reviewDAO.getAverageRating(productId);
						int count = reviewDAO.getReviewCount(productId);
						if (avg != null)
                            System.out.printf("Avg rating: %.1f from %d review%s%n", avg, count, count == 1 ? "" : "s");
					}

                    case Constants.REVIEW_MENU_SUBMIT -> {
						Review review = ReviewForm.inputNewReview();
						int id = reviewDAO.submitReview(review);
						System.out.println("Review submitted with ID: " + id);
					}

                    case Constants.REVIEW_MENU_UPDATE -> {
						int reviewId = ReviewForm.inputReviewId("update");
						int customerId = UserSession.getUserId();
						int rating = ReviewForm.inputNewRating();
						String comment = ReviewForm.inputNewComment();
						reviewDAO.updateReviewById(reviewId, customerId, rating, comment);
						System.out.println("Review updated successfully.");
					}

                    case Constants.REVIEW_MENU_DELETE -> {
						int reviewId = ReviewForm.inputReviewId("delete");
						Integer customerId = UserSession.isAdmin() ? null : UserSession.getUserId();
						reviewDAO.deleteReviewById(reviewId, customerId);
						System.out.println("Review deleted.");
					}

                    case Constants.MENU_BACK -> {
						System.out.println("Back to previous menu.");
						return;
					}

					default -> System.out.println("Invalid choice. Try again.");
				}
			} catch (DAOException e) {
				GlobalExceptionHandler.handle(e);
			}
		}
	}
}