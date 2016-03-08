package com.zebrait.jobs;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.zebrait.common.StockCollection;

public abstract class AbstractHandleStockJob implements Job {

	private Queue<String> stocks = new ConcurrentLinkedQueue<String>();
	private static ExecutorService threadPool = Executors.newFixedThreadPool(20);
	@Autowired
	private StockCollection stockCollection;

	@Override
	public void init() {
		synchronized (stockCollection) {
			for (String s : stockCollection.getStocks())
				stocks.add(s);
		}
	}

	@Override
	public void start() {
		while (!stocks.isEmpty()) {
			threadPool.submit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					String stock = stocks.remove();
					process(stock);
					return null;
				}
			});
		}
	}

	@Override
	public void clear() {
	}
}
