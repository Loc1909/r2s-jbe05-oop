package com.example.bikestores.form;

import com.example.bikestores.entity.Shipment;
import com.example.bikestores.util.Constants;
import com.example.bikestores.util.ScannerUtil;
import com.example.bikestores.util.ValidationUtil;

import java.time.LocalDate;

public class ShipmentForm {

    public static Shipment inputScheduleShipment() {
        int orderId = ScannerUtil.readInt("Enter order ID (paid): ");
        String address = ScannerUtil.readNonEmptyString("Enter shipping address: ");
        String carrier = ScannerUtil.readNonEmptyString("Enter carrier (e.g., DHL/UPS/VNPost): ");
        String method = ValidationUtil.readShippingMethod("Enter shipping method (standard/express/pickup): ");
        String tracking = ScannerUtil.readNonEmptyString("Enter tracking number: ");

        Shipment s = new Shipment();
        s.setOrderId(orderId);
        s.setShippingAddress(address);
        s.setCarrier(carrier);
        s.setShippingMethod(method);
        s.setTrackingNumber(tracking);
        s.setStatus(Constants.STATUS_PENDING);
        s.setShippedDate(null);
        s.setDeliveryDate(null);
        return s;
    }

    public static int inputShipmentId(String action) {
        return ScannerUtil.readInt("Enter shipment ID to " + action + ": ");
    }

    public static Shipment inputUpdateShipment() {
        int shipmentId = ScannerUtil.readInt("Enter shipment ID to update: ");
        String status = ValidationUtil.readShipmentStatus(
            String.format("Enter new status (%s/%s/%s/%s/%s/%s): ",
                Constants.STATUS_PENDING, Constants.STATUS_PROCESSING, Constants.STATUS_SHIPPED,
                Constants.STATUS_DELIVERED, Constants.STATUS_RETURNED, Constants.STATUS_CANCELLED)
        );
        LocalDate shippedDate = ValidationUtil.readDateOrNull("Enter shipped date (yyyy-mm-dd) or type 'null' for none: ");
        LocalDate deliveryDate = ValidationUtil.readDateOrNull("Enter delivery date (yyyy-mm-dd) or type 'null' for none: ");
        String tracking = ScannerUtil.readNonEmptyString("Enter tracking number: ");
        String address = ScannerUtil.readNonEmptyString("Enter shipping address: ");
        String method = ValidationUtil.readShippingMethod(
            String.format("Enter shipping method (%s/%s/%s): ",
                Constants.METHOD_STANDARD, Constants.METHOD_EXPRESS, Constants.METHOD_PICKUP)
        );
        String carrier = ScannerUtil.readNonEmptyString("Enter carrier (e.g., DHL/UPS/VNPost): ");

        Shipment s = new Shipment();
        s.setShipmentId(shipmentId);
        s.setStatus(status);
        s.setShippedDate(shippedDate);
        s.setDeliveryDate(deliveryDate);
        s.setTrackingNumber(tracking);
        s.setShippingAddress(address);
        s.setShippingMethod(method);
        s.setCarrier(carrier);
        return s;
    }

    public static int inputOrderId() {
        return ScannerUtil.readInt("Enter order ID to track: ");
    }

    public static String inputTrackingNumber() {
        return ScannerUtil.readNonEmptyString("Enter tracking number to track: ");
    }
}