package com.zebrait.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zebrait.entities.Stock;
import com.zebrait.repositories.StockRepository;

@Component
public class ResultHandler {

	@Autowired
	private StockRepository stockRepository;

	public void handle(Stock stock) {

	}
}
