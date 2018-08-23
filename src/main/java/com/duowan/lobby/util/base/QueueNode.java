package com.duowan.lobby.util.base;

import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class QueueNode<T> {

	private ReadWriteLock lock = new ReentrantReadWriteLock();

	private LinkedList<T> queue = new LinkedList<T>();

	public void leaveFirstNode() {
		try {
			lock.writeLock().lock();
			while (queue.size() > 2) {
				queue.removeLast();
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	public void addToQueue(T list) {
		try {
			lock.writeLock().lock();
			queue.addFirst(list);
		} finally {
			lock.writeLock().unlock();
		}
	}

	public T getFirstNode() {
		try {
			lock.readLock().lock();
			if (queue.size() > 0) {
				return queue.getFirst();
			}
		} finally {
			lock.readLock().unlock();
		}
		return null;
	}
}
