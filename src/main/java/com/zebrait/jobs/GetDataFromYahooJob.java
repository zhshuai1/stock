package com.zebrait.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zebrait.entities.Stock;
import com.zebrait.exceptions.JobFailException;
import com.zebrait.external.ResultHandler;
import com.zebrait.external.YahooClient;
import com.zebrait.external.YahooResultParser;

@Component
public class GetDataFromYahooJob extends AbstractHandleStockJob {
	@Autowired
	private YahooClient yahooClient;
	@Autowired
	private YahooResultParser yahooResultParser;
	@Autowired
	private ResultHandler resultHandler;

	@Override
	public void process(Object... obj) throws JobFailException {
		String code = (String) (obj[0]);
		System.out.println(code);
		String data = yahooClient.getData(code, 2015, 1, 1, 2016, 1, 1);
		System.out.println(data);
		Stock stock = yahooResultParser.parse(data);
		resultHandler.handle(stock);
	}
}
