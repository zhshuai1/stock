package com.zebrait.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zebrait.entities.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {

}
