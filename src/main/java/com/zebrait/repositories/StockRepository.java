package com.zebrait.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zebrait.entities.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {
	List<Stock> findByCodeAndDate(String code,Date date);
}
