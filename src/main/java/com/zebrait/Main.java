package com.zebrait;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zebrait.entities.Post;
import com.zebrait.repositories.PostRepository;

public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/application-context.xml");
		PostRepository postRepository = context.getBean("postRepository", PostRepository.class);
		System.out.println(postRepository);
		Post post = postRepository.findOne(Integer.valueOf(1));
		System.out.println(post.getTitle() + post.getPostId() + post.getPostDate());

		// StockRepository stockRepository = context.getBean("stockRepository",
		// StockRepository.class);
		// Stock stock = stockRepository.findOne(Integer.valueOf(1));
		// System.out.println(stock.getDate());
		//
		// GetDataFromYahooJob getDataFromYahooJob =
		// context.getBean("getDataFromYahooJob", GetDataFromYahooJob.class);
		// getDataFromYahooJob.init();
		// getDataFromYahooJob.start();
	}
}
