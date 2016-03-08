package com.zebrait.external;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zebrait.common.Month;
import com.zebrait.entities.Stock;
import com.zebrait.repositories.StockRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class YahooResultParser {
	@Autowired
	private StockRepository stockRepository;
	private static int count = 0;

	public void parseAndPersist(String code, String data) {
		log.info("begin to parse data in parse and persist for " + code);
		Pattern pattern = Pattern.compile(
				"<tr><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td></tr>");
		Matcher matcher = pattern.matcher(data);
		while (matcher.find()) {
			log.info("pattern matched for " + code);
			Date date = parseDate(matcher.group(1));
			log.info("parse date " + code);
			float begin = Float.parseFloat(matcher.group(2));
			log.info("parse begin " + code);
			float high = Float.parseFloat(matcher.group(3));
			float low = Float.parseFloat(matcher.group(4));
			float end = Float.parseFloat(matcher.group(5));
			float delta = (end - begin) * 100 / begin;
			Stock stock = new Stock(code, date, begin, end, high, low, delta);
			log.info("The stock is " + stock);

			// stockRepository.save(stock);
			// stock = null;
			System.out.println(code + "------------" + matcher.group(1) + ++count);
		}

	}

	private Date parseDate(String dateString) {
		log.info("Begin to parse date " + dateString);
		Pattern pattern = Pattern.compile("(.{3}) (.{2}), (.{4})");
		Matcher matcher = pattern.matcher(dateString);
		if (matcher.find()) {
			log.info("pattern found in dateString...");
			int month = Month.valueOf(matcher.group(1)).getIndex();
			int day = Integer.parseInt(matcher.group(2));
			int year = Integer.parseInt(matcher.group(3)) - 1900;
			Date date = new Date(year, month, day);
			date.setHours(15);
			return date;
		}
		return null;
	}

}
