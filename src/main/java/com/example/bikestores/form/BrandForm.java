package com.example.bikestores.form;

import com.example.bikestores.entity.Brand;
import com.example.bikestores.util.ScannerUtil;
import com.example.bikestores.util.ValidationUtil;

public class BrandForm {

    public static Brand inputNewBrand() {
        while (true) {
            String name = ScannerUtil.readNonEmptyString("Enter brand name: ");
            if (ValidationUtil.isValidString(name)) {
                return new Brand(0, name);
            } else {
                System.out.println("Brand name must not be empty.");
            }
        }
    }

    public static Brand inputUpdateBrand() {
        int id = ScannerUtil.readInt("Enter brand ID to update: ");
        String name = ScannerUtil.readNonEmptyString("Enter new brand name: ");
        return new Brand(id, name);
    }

    public static int inputBrandId(String action) {
        return ScannerUtil.readInt("Enter brand ID to " + action + ": ");
    }
}