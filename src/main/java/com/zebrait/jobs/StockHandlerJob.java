package com.zebrait.jobs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zebrait.common.StockCollection;
import com.zebrait.processors.StockProcessor;
import com.zebrait.tasks.Task;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class StockHandlerJob {
	@Autowired
	private StockCollection stockCollection;
	@Autowired
	private StockProcessor stockProcessor;
	private Queue<Task<String>> tasks = new LinkedList<>();
	private ExecutorService executor = Executors.newFixedThreadPool(10);
	private List<Future<Task<String>>> futures = new LinkedList<>();
	private List<String> dlList = new LinkedList<>();
	private List<String> finishedList = new LinkedList<>();
	private int year, month, day, days;

	public void run(int year, int month, int day, int days) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.days = days;
		init();
		work();
		dump();
	}

	private void init() {
		for (String stock : stockCollection.getStocks()) {
			Task<String> task = new Task<String>(stock);
			tasks.add(task);
		}
		log.info("Stocks to be handled are: {}", stockCollection.getStocks());
	}

	private void work() {
		while (!tasks.isEmpty()) {
			Task<String> task = tasks.remove();
			futures.add(submitTask(task));
		}
		while (!futures.isEmpty()) {
			List<Future<Task<String>>> newFutures = new LinkedList<>();
			Iterator<Future<Task<String>>> iterator = futures.iterator();
			while (iterator.hasNext()) {
				Future<Task<String>> future = iterator.next();
				if (future.isDone()) {
					try {
						Task<String> task = future.get();
						switch (task.getStatus()) {
						case FAILED:
							dlList.add(task.getData());
							log.info("Process stock {} failed, add it into dlq", task.getData());
							break;
						case RETRIABLE:
							newFutures.add(submitTask(task));
							log.info("Retry for stock {} for the {} time", task.getData(), task.getFailureCount());
							break;
						case FINISHED:
							log.info("Process stock {} finished, add it into finished q", task.getData());
							finishedList.add(task.getData());
							break;
						default:
							break;
						}
					} catch (ExecutionException e) {
						log.warn(e);
					} catch (Exception e) {
						log.warn(e);
					}
					iterator.remove();
				}
			}
			futures.addAll(newFutures);
		}
		executor.shutdown();
	}

	private void dump() {
		log.warn("The unhandled stocks are: \n{}", dlList);
		log.warn("The finished stocks are: \n{}", finishedList);
	}

	private Future<Task<String>> submitTask(Task<String> task) {
		return executor.submit(new Callable<Task<String>>() {
			@Override
			public Task<String> call() throws Exception {
				try {
					stockProcessor.processStock(task.getData(), year, month, day, days);
					task.finished();
					return task;
				} catch (Exception e) {
					log.warn("Exception encountered when processing stock {}, reason is {}", task.getData(), e);
					task.retried();
					return task;
				}

			}
		});

	}
}
