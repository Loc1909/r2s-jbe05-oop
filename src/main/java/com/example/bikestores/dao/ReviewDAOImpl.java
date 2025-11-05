package com.example.bikestores.dao;

import com.example.bikestores.entity.Review;
import com.example.bikestores.exception.DAOException;
import com.example.bikestores.util.JDBCUtil;
import com.example.bikestores.auth.UserSession;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

	@Override
	public int submitReview(Review review) throws DAOException {
		String existsSql = "SELECT 1 FROM reviews WHERE customer_id=? AND product_id=?";
		String insertSql = "INSERT INTO reviews (customer_id, product_id, rating, comment, review_date) VALUES (?,?,?,?,CURRENT_DATE)";
		try (Connection conn = JDBCUtil.getConnection()) {
            // Check xem đã có review cho product này chưa
			try (PreparedStatement ex = conn.prepareStatement(existsSql)) {
				ex.setInt(1, review.getCustomerId());
				ex.setInt(2, review.getProductId());
				ResultSet rs = ex.executeQuery();
				if (rs.next()) {
					throw new DAOException("You have already reviewed this product.", null);
				}
			}
			
			try (PreparedStatement ins = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
				ins.setInt(1, review.getCustomerId());
				ins.setInt(2, review.getProductId());
				ins.setInt(3, review.getRating());
				ins.setString(4, review.getComment());
				ins.executeUpdate();
				try (ResultSet keys = ins.getGeneratedKeys()) {
					if (keys.next())
                        return keys.getInt(1);
				}
				throw new DAOException("Failed to retrieve generated review ID.", null);
			}
		} catch (SQLException e) {
			throw new DAOException("Failed to submit review.", e);
		}
	}

	@Override
	public List<Review> findByProduct(int productId, Integer minRating, boolean sortNewest) throws DAOException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT review_id, customer_id, product_id, rating, comment, review_date FROM reviews WHERE product_id=?");
		if (minRating != null)
            sb.append(" AND rating >= ?");
		sb.append(sortNewest ? " ORDER BY review_date DESC, review_id DESC" : " ORDER BY review_date ASC, review_id ASC");
		String sql = sb.toString();

		List<Review> list = new ArrayList<>();
		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, productId);
			if (minRating != null)
                ps.setInt(2, minRating);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
                list.add(mapRow(rs));
			return list;
		} catch (SQLException e) {
			throw new DAOException("Failed to get product reviews.", e);
		}
	}

	@Override
	public Double getAverageRating(int productId) throws DAOException {
		String sql = "SELECT AVG(rating) AS avg_rating FROM reviews WHERE product_id=?";
		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, productId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				double avg = rs.getDouble("avg_rating");
				if (rs.wasNull())
                    return null;
				return avg;
			}
			return null;
		} catch (SQLException e) {
			throw new DAOException("Failed to calculate average rating.", e);
		}
	}

	@Override
	public int getReviewCount(int productId) throws DAOException {
		String sql = "SELECT COUNT(*) AS review_count FROM reviews WHERE product_id=?";
		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, productId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("review_count");
			}
			return 0;
		} catch (SQLException e) {
			throw new DAOException("Failed to get review count.", e);
		}
	}

	@Override
	public void updateReviewById(int reviewId, int customerId, Integer rating, String comment) throws DAOException {
		String ownerSql = "SELECT customer_id FROM reviews WHERE review_id=?";
		String updSql = "UPDATE reviews SET rating=?, comment=? WHERE review_id=?";
		try (Connection conn = JDBCUtil.getConnection()) {
            // Check xem phải customer này là owner của review không
			try (PreparedStatement os = conn.prepareStatement(ownerSql)) {
				os.setInt(1, reviewId);
				ResultSet rs = os.executeQuery();

				if (!rs.next())
                    throw new DAOException("Review not found.", null);

				int owner = rs.getInt(1);
				if (owner != customerId)
                    throw new DAOException("Permission denied: not your review.", null);
			}

			try (PreparedStatement us = conn.prepareStatement(updSql)) {
				us.setInt(1, rating);
				us.setString(2, comment);
				us.setInt(3, reviewId);
				us.executeUpdate();
			}
		} catch (SQLException e) {
			throw new DAOException("Failed to update review.", e);
		}
	}

	@Override
	public void deleteReviewById(int reviewId, Integer customerId) throws DAOException {
		String ownerSql = "SELECT customer_id FROM reviews WHERE review_id=?";
		String delSql = "DELETE FROM reviews WHERE review_id=?";
		try (Connection conn = JDBCUtil.getConnection()) {
			int owner;

			try (PreparedStatement os = conn.prepareStatement(ownerSql)) {
				os.setInt(1, reviewId);
				ResultSet rs = os.executeQuery();
				if (!rs.next())
                    throw new DAOException("Review not found.", null);
				owner = rs.getInt(1);
			}

            // Phải là owner của review hoặc là admin mới xóa được
			boolean isAdmin = UserSession.isAdmin();
			if (!isAdmin && (customerId == null || owner != customerId)) {
				throw new DAOException("Permission denied: cannot delete this review.", null);
			}

			try (PreparedStatement ds = conn.prepareStatement(delSql)) {
				ds.setInt(1, reviewId);
				ds.executeUpdate();
			}

		} catch (SQLException e) {
			throw new DAOException("Failed to delete review.", e);
		}
	}

	private Review mapRow(ResultSet rs) throws SQLException {
		int id = rs.getInt("review_id");
		int customerId = rs.getInt("customer_id");
		int productId = rs.getInt("product_id");
		int rating = rs.getInt("rating");
		String comment = rs.getString("comment");
		Date d = rs.getDate("review_date");
		LocalDate reviewDate = d != null ? d.toLocalDate() : null;
		return new Review(id, customerId, productId, rating, comment, reviewDate);
	}
}