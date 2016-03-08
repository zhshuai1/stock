package com.zebrait;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/application-context.xml")
public class TestAll {
	@Test
	public void test() throws Exception {
		// ApplicationContext context = new
		// ClassPathXmlApplicationContext("META-INF/application-context.xml");
		// YahooClient yahooClient = context.getBean("yahooClient",
		// YahooClient.class);
		// YahooResultParser yahooResultParser =
		// context.getBean("yahooResultParser", YahooResultParser.class);
		// List<String> stocks = Arrays.asList("600030", "600600", "600601",
		// "600602");
		// for (String stock : stocks) {
		// String data = yahooClient.getData(stock, 2015, 2, 21, 2016, 3, 3);
		// yahooResultParser.parseAndPersist(stock, data);
		// }
	}

}
