package com.zebrait.processors;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zebrait.common.Month;
import com.zebrait.entities.Stock;
import com.zebrait.exceptions.JobFailException;
import com.zebrait.external.YahooClient;
import com.zebrait.repositories.StockRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class StockProcessor {
	@Autowired
	private YahooClient yahooClient;
	@Autowired
	private StockRepository stockRepository;

	private ThreadLocal<Long> timer = new ThreadLocal<>();

	private static final String PATTERN = "<tr><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td></tr>";

	private static final int BATCH_SIZE = 60;

	public void processStock(String code, int year, int month, int day, int days) throws Exception {
		int counter = (days - 1) / BATCH_SIZE;
		for (int i = 0; i < counter; ++i) {
			Calendar date = Calendar.getInstance();
			date.set(year, month, day);
			date.add(Calendar.DATE, -BATCH_SIZE * i);
			processStockPartial(code, date, BATCH_SIZE);
		}
		Calendar date = Calendar.getInstance();
		date.set(year, month, day);
		date.add(Calendar.DATE, -BATCH_SIZE * counter);
		processStockPartial(code, date, days - BATCH_SIZE * counter);
	}

	private void processStockPartial(String code, Calendar date, int days) throws Exception {
		int year2 = date.get(Calendar.YEAR);
		int month2 = date.get(Calendar.MONTH);
		int day2 = date.get(Calendar.DATE);
		date.add(Calendar.DATE, -days);
		int year1 = date.get(Calendar.YEAR);
		int month1 = date.get(Calendar.MONTH);
		int day1 = date.get(Calendar.DATE);
		processStockPartial(code, year1, month1, day1, year2, month2, day2);
	}

	private void processStockPartial(String code, int year1, int month1, int day1, int year2, int month2, int day2)
			throws Exception {
		log.error("{} {} {} {} {} {} {}", code, year1, month1, day1, year2, month2, day2);
		timer.set(System.currentTimeMillis());
		String data = null;
		try {
			data = yahooClient.getData(code, year1, month1, day1, year2, month2, day2);
		} catch (Exception e) {
			throw new JobFailException("Failed to get data from yahoo for " + code, e);
		}
		log.info("get data for {} succeed, time eclapsed is {}", code, System.currentTimeMillis() - timer.get());
		timer.set(System.currentTimeMillis());
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(data);
		while (matcher.find()) {
			float begin = Float.parseFloat(matcher.group(2));
			float high = Float.parseFloat(matcher.group(3));
			float low = Float.parseFloat(matcher.group(4));
			float end = Float.parseFloat(matcher.group(5));
			float delta = (end - begin) * 100 / begin;
			Date date = parseDate(matcher.group(1));

			List<Stock> stocksInDb = stockRepository.findByCodeAndDate(code, date);
			if (!stocksInDb.isEmpty()) {
				continue;
			}

			Stock stock = new Stock(code, date, begin, end, high, low, delta);
			try {
				stockRepository.save(stock);
			} catch (Exception e) {
				throw new JobFailException("Failed to save stock " + stock + " in " + date, e);
			}
		}
		log.info("Persist for stock {} successfully, and time eclapse is {}", code,
				System.currentTimeMillis() - timer.get());

	}

	private Date parseDate(String dateString) {
		String[] strings = dateString.split(" ");
		int month = Month.valueOf(strings[0]).getIndex();
		int day = Integer.parseInt(strings[1].substring(0, strings[1].length() - 1));
		int year = Integer.parseInt(strings[2]) - 1900;
		return new Date(year, month, day);
	}
}
