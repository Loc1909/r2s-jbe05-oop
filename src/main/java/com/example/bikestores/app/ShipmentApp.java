package com.example.bikestores.app;

import com.example.bikestores.dao.ShipmentDAO;
import com.example.bikestores.dao.ShipmentDAOImpl;
import com.example.bikestores.entity.Shipment;
import com.example.bikestores.exception.DAOException;
import com.example.bikestores.exception.GlobalExceptionHandler;
import com.example.bikestores.form.ShipmentForm;
import com.example.bikestores.util.Constants;
import com.example.bikestores.util.StringUtil;

import java.util.List;
import java.util.Scanner;

public class ShipmentApp {
    public static void run() {
        ShipmentDAO shipmentDAO = new ShipmentDAOImpl();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== SHIPMENT MENU =====");
            System.out.println(Constants.SHIPMENT_MENU_LIST + ". List all shipments");
            System.out.println(Constants.SHIPMENT_MENU_SCHEDULE + ". Schedule a shipment");
            System.out.println(Constants.SHIPMENT_MENU_TRACK_ORDER + ". Track by order ID");
            System.out.println(Constants.SHIPMENT_MENU_TRACK_TRACKING + ". Track by tracking number");
            System.out.println(Constants.SHIPMENT_MENU_UPDATE + ". Update shipment");
            System.out.println(Constants.SHIPMENT_MENU_CANCEL + ". Cancel shipment (" + Constants.STATUS_PENDING + "/" + Constants.STATUS_PROCESSING + " only)");
            System.out.println(Constants.SHIPMENT_MENU_DELETE + ". Delete shipment (" + Constants.STATUS_PENDING + "/" + Constants.STATUS_PROCESSING + " only)");
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
                    case Constants.SHIPMENT_MENU_LIST -> {
                        List<Shipment> list = shipmentDAO.findAll();
                        System.out.println(Constants.SHIPMENT_HEADER);
                        for (Shipment s : list) {
                            System.out.printf(Constants.SHIPMENT_ROW_FORMAT + "%n",
                                    s.getShipmentId(), s.getOrderId(), StringUtil.truncate(s.getShippingAddress(),25), s.getShippingMethod(), s.getCarrier(),
                                    s.getTrackingNumber(), s.getStatus(), s.getShippedDate(), s.getDeliveryDate());
                        }
                    }
                    case Constants.SHIPMENT_MENU_SCHEDULE -> {
                        Shipment toSchedule = ShipmentForm.inputScheduleShipment();
                        int id = shipmentDAO.scheduleShipment(toSchedule);
                        System.out.println("Shipment scheduled with ID: " + id);
                    }
                    case Constants.SHIPMENT_MENU_TRACK_ORDER -> {
                        int orderId = ShipmentForm.inputOrderId();
                        Shipment s = shipmentDAO.findByOrderId(orderId);
                        System.out.println(s != null ? s : "Shipment not found");
                    }
                    case Constants.SHIPMENT_MENU_TRACK_TRACKING -> {
                        String tracking = ShipmentForm.inputTrackingNumber();
                        Shipment s = shipmentDAO.findByTrackingNumber(tracking);
                        System.out.println(s != null ? s : "Shipment not found");
                    }
                    case Constants.SHIPMENT_MENU_UPDATE -> {
                        Shipment upd = ShipmentForm.inputUpdateShipment();
                        shipmentDAO.updateShipment(upd);
                        System.out.println("Shipment updated successfully.");
                    }
                    case Constants.SHIPMENT_MENU_CANCEL -> {
                        int id = ShipmentForm.inputShipmentId("cancel");
                        shipmentDAO.cancelShipment(id);
                        System.out.println("Shipment cancelled.");
                    }
                    case Constants.SHIPMENT_MENU_DELETE -> {
                        int id = ShipmentForm.inputShipmentId("delete");
                        shipmentDAO.deleteShipment(id);
                        System.out.println("Shipment deleted.");
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


