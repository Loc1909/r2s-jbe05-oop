package com.example.bikestores.dao;

import com.example.bikestores.entity.Shipment;
import com.example.bikestores.exception.DAOException;
import com.example.bikestores.util.Constants;
import com.example.bikestores.util.JDBCUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAOImpl implements ShipmentDAO {

    @Override
    public int scheduleShipment(Shipment shipment) throws DAOException {
        String checkSql = "SELECT o.order_id FROM orders o WHERE o.order_id=? AND UPPER(o.status)=UPPER('" + Constants.ORDER_STATUS_PAID + "')";
        String insertSql = "INSERT INTO shipments (order_id, carrier, shipping_address, shipping_method, tracking_number, status, shipped_date, delivery_date) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = JDBCUtil.getConnection()) {
            // Check xem order_id có tồn tại và paid chưa
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, shipment.getOrderId());
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    throw new DAOException("Order is not eligible for shipping (not found or not PAID).", null);
                }
            }

            // Tạo shipment
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) { // Statement.RETURN_GENERATED_KEYS: báo cho JDBC biết rằng sau khi thực thi, ta muốn lấy lại khóa tự sinh.
                insertStmt.setInt(1, shipment.getOrderId());
                insertStmt.setString(2, shipment.getCarrier());
                insertStmt.setString(3, shipment.getShippingAddress());
                insertStmt.setString(4, shipment.getShippingMethod());
                insertStmt.setString(5, shipment.getTrackingNumber());
                insertStmt.setString(6, shipment.getStatus() == null ? Constants.STATUS_PENDING : shipment.getStatus());

                if (shipment.getShippedDate() != null) {
                    insertStmt.setDate(7, Date.valueOf(shipment.getShippedDate()));
                } else {
                    insertStmt.setNull(7, Types.DATE);
                }

                if (shipment.getDeliveryDate() != null) {
                    insertStmt.setDate(8, Date.valueOf(shipment.getDeliveryDate()));
                } else {
                    insertStmt.setNull(8, Types.DATE);
                }

                insertStmt.executeUpdate();
                try (ResultSet keys = insertStmt.getGeneratedKeys()) { // lấy giá trị của cột khóa chính tự tăng
                    if (keys.next()) {
                        return keys.getInt(1); // lấy giá trị cột 1 (shipment_id)
                    }
                }
                throw new DAOException("Failed to retrieve generated shipment ID.", null);
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to schedule shipment.", e);
        }
    }

    @Override
    public Shipment findById(int shipmentId) throws DAOException {
        String sql = baseSelect() + " WHERE s.shipment_id=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new DAOException("Failed to find shipment by id.", e);
        }
    }

    @Override
    public Shipment findByOrderId(int orderId) throws DAOException {
        String sql = baseSelect() + " WHERE s.order_id=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new DAOException("Failed to find shipment by order id.", e);
        }
    }

    @Override
    public Shipment findByTrackingNumber(String trackingNumber) throws DAOException {
        String sql = baseSelect() + " WHERE s.tracking_number=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, trackingNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;
        } catch (SQLException e) {
            throw new DAOException("Failed to find shipment by tracking number.", e);
        }
    }

    @Override
    public List<Shipment> findAll() throws DAOException {
        String sql = baseSelect();
        List<Shipment> list = new ArrayList<>();
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
            return list;
        } catch (SQLException e) {
            throw new DAOException("Failed to list shipments.", e);
        }
    }

    @Override
    public void updateShipment(Shipment shipment) throws DAOException {
        // Not allow update if delivered
        String checkSql = "SELECT status FROM shipments WHERE shipment_id=?";
        String updateSql = "UPDATE shipments SET status=?, shipped_date=?, delivery_date=?, tracking_number=?, shipping_address=?, shipping_method=?, carrier=? WHERE shipment_id=?";
        try (Connection conn = JDBCUtil.getConnection()) {
            // Check xem status == delivered; nếu =, ko cho update
            try (PreparedStatement cs = conn.prepareStatement(checkSql)) {
                cs.setInt(1, shipment.getShipmentId());
                ResultSet rs = cs.executeQuery();

                if (!rs.next())
                    throw new DAOException("Shipment not found.", null);

                String current = rs.getString("status"); // lấy status
                if (Constants.STATUS_DELIVERED.equalsIgnoreCase(current)) {
                    throw new DAOException("Cannot update a delivered shipment.", null);
                }
            }

            // Update
            try (PreparedStatement us = conn.prepareStatement(updateSql)) {
                us.setString(1, shipment.getStatus());
                if (shipment.getShippedDate() != null) {
                    us.setDate(2, Date.valueOf(shipment.getShippedDate()));
                } else {
                    us.setNull(2, Types.DATE);
                }
                if (shipment.getDeliveryDate() != null) {
                    us.setDate(3, Date.valueOf(shipment.getDeliveryDate()));
                } else {
                    us.setNull(3, Types.DATE);
                }
                us.setString(4, shipment.getTrackingNumber());
                us.setString(5, shipment.getShippingAddress());
                us.setString(6, shipment.getShippingMethod());
                us.setString(7, shipment.getCarrier());
                us.setInt(8, shipment.getShipmentId());
                us.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to update shipment.", e);
        }
    }

    @Override
    public void cancelShipment(int shipmentId) throws DAOException {
        String checkSql = "SELECT status FROM shipments WHERE shipment_id=?";
        String cancelSql = "UPDATE shipments SET status='" + Constants.STATUS_CANCELLED + "' WHERE shipment_id=?";
        try (Connection conn = JDBCUtil.getConnection()) {
            // Must be pending or processing
            try (PreparedStatement cs = conn.prepareStatement(checkSql)) {
                cs.setInt(1, shipmentId);
                ResultSet rs = cs.executeQuery();
                if (!rs.next())
                    throw new DAOException("Shipment not found.", null);
                String current = rs.getString(1);
                if (!(Constants.STATUS_PENDING.equalsIgnoreCase(current) || Constants.STATUS_PROCESSING.equalsIgnoreCase(current))) {
                    throw new DAOException("Only pending/processing shipments can be cancelled.", null);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(cancelSql)) {
                ps.setInt(1, shipmentId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to cancel shipment.", e);
        }
    }

    @Override
    public void deleteShipment(int shipmentId) throws DAOException {
        String checkSql = "SELECT status FROM shipments WHERE shipment_id=?";
        String deleteSql = "DELETE FROM shipments WHERE shipment_id=?";
        try (Connection conn = JDBCUtil.getConnection()) {
            // Must be pending or processing
            try (PreparedStatement cs = conn.prepareStatement(checkSql)) {
                cs.setInt(1, shipmentId);
                ResultSet rs = cs.executeQuery();
                if (!rs.next())
                    throw new DAOException("Shipment not found.", null);
                String current = rs.getString(1);
                if (!(Constants.STATUS_PENDING.equalsIgnoreCase(current) || Constants.STATUS_PROCESSING.equalsIgnoreCase(current))) {
                    throw new DAOException("Delete blocked. Status must be pending/processing.", null);
                }
            }

            try (PreparedStatement ds = conn.prepareStatement(deleteSql)) {
                ds.setInt(1, shipmentId);
                ds.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to delete shipment.", e);
        }
    }

    private String baseSelect() {
        return "SELECT s.shipment_id, s.order_id, s.shipping_address, s.shipping_method, s.carrier, s.tracking_number, s.status, s.shipped_date, s.delivery_date FROM shipments s";
    }

    private Shipment mapRow(ResultSet rs) throws SQLException {
        int shipmentId = rs.getInt("shipment_id");
        int orderId = rs.getInt("order_id");
        String shippingAddress = rs.getString("shipping_address");
        String shippingMethod = rs.getString("shipping_method");
        String carrier = rs.getString("carrier");
        String trackingNumber = rs.getString("tracking_number");
        String status = rs.getString("status");
        Date shipped = rs.getDate("shipped_date");
        Date delivered = rs.getDate("delivery_date");
        LocalDate shippedDate = shipped != null ? shipped.toLocalDate() : null;
        LocalDate deliveryDate = delivered != null ? delivered.toLocalDate() : null;
        return new Shipment(shipmentId, orderId, shippingAddress, shippingMethod, trackingNumber, status, shippedDate, carrier, deliveryDate);
    }
}