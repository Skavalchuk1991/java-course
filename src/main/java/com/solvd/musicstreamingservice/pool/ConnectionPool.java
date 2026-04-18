package com.solvd.musicstreamingservice.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Thread-safe Connection Pool with lazy initialization (Singleton).
 * Uses BlockingQueue from java.util.concurrent.
 * getConnection() blocks if no connections available.
 */
public class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    private static volatile ConnectionPool instance;

    private final BlockingQueue<Connection> pool;
    private final int size;

    private ConnectionPool(int size) {
        this.size = size;
        this.pool = new ArrayBlockingQueue<>(size);
        for (int i = 1; i <= size; i++) {
            pool.add(new Connection(i));
        }
        LOGGER.info("ConnectionPool initialized with {} connections", size);
    }

    /**
     * Lazy initialization with double-checked locking (thread-safe singleton).
     */
    public static ConnectionPool getInstance(int size) {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool(size);
                }
            }
        }
        return instance;
    }

    /**
     * Gets a connection from the pool. Blocks if none available.
     */
    public Connection getConnection() {
        try {
            LOGGER.info("Thread '{}' requesting connection...", Thread.currentThread().getName());
            Connection connection = pool.take(); // blocks if pool is empty
            connection.setInUse(true);
            LOGGER.info("Thread '{}' acquired {}", Thread.currentThread().getName(), connection);
            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for connection", e);
        }
    }

    /**
     * Returns a connection back to the pool.
     */
    public void releaseConnection(Connection connection) {
        try {
            connection.setInUse(false);
            pool.put(connection); // blocks if pool is full (shouldn't happen)
            LOGGER.info("Thread '{}' released {}", Thread.currentThread().getName(), connection);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while releasing connection", e);
        }
    }

    public int getAvailableCount() {
        return pool.size();
    }

    public int getSize() {
        return size;
    }
}
