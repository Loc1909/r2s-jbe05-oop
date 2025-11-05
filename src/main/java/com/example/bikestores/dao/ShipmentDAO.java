package com.example.bikestores.dao;

import com.example.bikestores.entity.Shipment;
import com.example.bikestores.exception.DAOException;

import java.util.List;

public interface ShipmentDAO {
    int scheduleShipment(Shipment shipment) throws DAOException;
    Shipment findById(int shipmentId) throws DAOException;
    Shipment findByOrderId(int orderId) throws DAOException;
    Shipment findByTrackingNumber(String trackingNumber) throws DAOException;
    List<Shipment> findAll() throws DAOException;
    void updateShipment(Shipment shipment) throws DAOException;
    void cancelShipment(int shipmentId) throws DAOException;
    void deleteShipment(int shipmentId) throws DAOException;
}