package com.zebrait.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zebrait.exceptions.JobFailException;
import com.zebrait.external.YahooClient;
import com.zebrait.external.YahooResultParser;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class GetDataFromYahooJob extends AbstractHandleStockJob {
	@Autowired
	private YahooClient yahooClient;
	@Autowired
	private YahooResultParser yahooResultParser;

	@Override
	public void process(Object... obj) throws JobFailException {
		String code = (String) (obj[0]);
		log.info("\n\n\n\n\n\nBegin to get data for " + code);
		String data = yahooClient.getData(code, 2015, 1, 1, 2016, 1, 1);
		log.info("begin to parse and persist data for " + code);
		yahooResultParser.parseAndPersist(code, data);
		log.info("Finished processing data for " + code);
	}
}
