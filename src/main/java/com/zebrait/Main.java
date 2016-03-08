package com.zebrait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.stream.IntStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zebrait.entities.Stock;
import com.zebrait.jobs.GetDataFromYahooJob;
import com.zebrait.repositories.StockRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/application-context.xml");
		for (int i = 0; i < 10000; ++i) {
			URL url;
			String temp;
			StringBuffer sb = new StringBuffer();
			try {
				url = new URL("http://finance.yahoo.com/q/hp?s=603799.SS&a=05&b=11&c=2010&d=00&e=29&f=2016&g=d");
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "gbk"));// 读取网页全部内容
				while ((temp = in.readLine()) != null) {
					sb.append(temp);
				}
				in.close();
			} catch (final MalformedURLException me) {
				System.out.println("你输入的URL格式有问题!");
				me.getMessage();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			System.out.println(sb.toString());
		}

		{
			StockRepository stockRepository = context.getBean("stockRepository", StockRepository.class);
			IntStream.range(0, 10000).forEach(i -> {
				Stock stock = new Stock("" + 600000 + i, new Date(i + 100000), i + .3f, i + .4f, i + .3f, i + .4f,
						2.2f);
				stockRepository.save(stock);
				log.info("Save for stock {} successfully!", i);
			});

		}

		System.exit(0);

		// System.exit(0);
		// StockRepository stockRepository = context.getBean("stockRepository",
		// StockRepository.class);
		// Stock stock = stockRepository.findOne(Integer.valueOf(1));
		// System.out.println(stock.getDate());

		GetDataFromYahooJob getDataFromYahooJob = context.getBean("getDataFromYahooJob", GetDataFromYahooJob.class);
		getDataFromYahooJob.init();
		getDataFromYahooJob.start();
	}
}
