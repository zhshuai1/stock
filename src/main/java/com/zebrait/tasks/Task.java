package com.zebrait.tasks;

import lombok.Getter;

public class Task<T> {
	private static final int MAX_RETRY = 3;
	@Getter
	private T data;
	@Getter
	private Status status;
	@Getter
	private int failureCount;

	public Task(T data) {
		this.data = data;
		failureCount = 0;
		status = Status.READY;
	}

	public void retried() {
		if (failureCount < MAX_RETRY) {
			++failureCount;
			status = Status.RETRIABLE;
		} else {
			status = Status.FAILED;
		}

	}

	public void failed() {
		status = Status.FAILED;
	}

	public void finished() {
		status = Status.FINISHED;
	}

	public static enum Status {
		READY, FAILED, RETRIABLE, FINISHED;
	}
}
