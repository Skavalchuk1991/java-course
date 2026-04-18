package com.solvd.musicstreamingservice.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Thread created by extending Thread class.
 */
public class ConnectionThread extends Thread {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionThread.class);

    private final ConnectionPool connectionPool;

    public ConnectionThread(String name, ConnectionPool connectionPool) {
        super(name);
        this.connectionPool = connectionPool;
    }

    @Override
    public void run() {
        Connection connection = connectionPool.getConnection();
        try {
            connection.create("User-" + getName());
            connection.get(1);
            Thread.sleep(1000); // simulate work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Thread {} interrupted", getName());
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }
}
