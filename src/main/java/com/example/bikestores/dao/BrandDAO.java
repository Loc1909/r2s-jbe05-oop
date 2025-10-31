package com.example.bikestores.dao;

import com.example.bikestores.entity.Brand;
import com.example.bikestores.exception.DAOException;
import java.util.List;

public interface BrandDAO {
    void insert(Brand brand) throws DAOException;
    void update(Brand brand) throws DAOException;
    void delete(int brandId) throws DAOException;
    Brand findById(int brandId) throws DAOException;
    List<Brand> findAll() throws DAOException;
}