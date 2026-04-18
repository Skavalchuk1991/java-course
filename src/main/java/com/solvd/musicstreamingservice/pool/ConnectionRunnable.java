package com.solvd.musicstreamingservice.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Thread created by implementing Runnable interface.
 */
public class ConnectionRunnable implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionRunnable.class);

    private final ConnectionPool connectionPool;

    public ConnectionRunnable(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void run() {
        Connection connection = connectionPool.getConnection();
        try {
            connection.update(1, "Updated by " + Thread.currentThread().getName());
            connection.delete(99);
            Thread.sleep(1000); // simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Thread {} interrupted", Thread.currentThread().getName());
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }
}
