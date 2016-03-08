package com.zebrait;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zebrait.common.Month;
import com.zebrait.entities.Stock;
import com.zebrait.exceptions.JobFailException;
import com.zebrait.external.YahooClient;
import com.zebrait.jobs.StockHandlerJob;
import com.zebrait.processors.StockProcessor;
import com.zebrait.repositories.StockRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main2 {
	public static void main(String[] args) {
		// new Main2().test();
		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/application-context.xml");
		StockProcessor stockProcessor = context.getBean("stockProcessor", StockProcessor.class);
		StockHandlerJob stockHandlerJob = context.getBean("stockHandlerJob", StockHandlerJob.class);
		stockHandlerJob.run(2016, 3, 5, 1000);
		for (int i = 0; i < 0; ++i) {
			try {
				stockProcessor.processStock("" + (600200 + i), 2016, 3, 8, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void test() {
		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/application-context.xml");
		YahooClient yahooClient = context.getBean("yahooClient", YahooClient.class);
		StockRepository stockRepository = context.getBean("stockRepository", StockRepository.class);
		for (int i = 0; i < 10000; ++i) {
			String data = null;
			String code = "" + (600000 + i);
			log.info("begin to handle {}", code);
			try {
				data = yahooClient.getData(code, 2015, 1, 12, 2016, 3, 8);
			} catch (JobFailException e) {
				e.printStackTrace();
			}
			log.info("get data for {} succeed: {}", code, data);
			Pattern pattern = Pattern.compile(
					"<tr><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td><td[^<>]*>([^<>]*)</td></tr>");
			Matcher matcher = pattern.matcher(data);
			while (matcher.find()) {
				log.info("matched in html");
				float begin = Float.parseFloat(matcher.group(2));
				float end = Float.parseFloat(matcher.group(3));
				float high = Float.parseFloat(matcher.group(4));
				float low = Float.parseFloat(matcher.group(5));
				float delta = (end - begin) * 100 / begin;
				Date date = parseDate(matcher.group(1));
				Stock stock = new Stock(code, date, begin, end, high, low, delta);
				log.info(stock);
				try {
					stockRepository.save(stock);
				} catch (Exception e) {
					log.warn("Exceptions encountered when saving {}", stock);
				}

			}
		}
	}

	private Date parseDate(String dateString) {
		String[] strings = dateString.split(" ");
		int month = Month.valueOf(strings[0]).getIndex();
		int day = Integer.parseInt(strings[1].substring(0, strings[1].length() - 1));
		int year = Integer.parseInt(strings[2]) - 1900;
		return new Date(year, month, day);
	}
}
