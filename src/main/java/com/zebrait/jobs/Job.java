package com.zebrait.jobs;

public interface Job {
	void init();

	void start();

	void process(Object... obj)throws Exception;

	void clear();
}
