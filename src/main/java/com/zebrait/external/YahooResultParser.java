package com.zebrait.external;

import org.springframework.stereotype.Component;

import com.zebrait.entities.Stock;

@Component
public class YahooResultParser {

	public Stock parse(String data) {
		Stock stock = new Stock();
		return stock;
	}

}
