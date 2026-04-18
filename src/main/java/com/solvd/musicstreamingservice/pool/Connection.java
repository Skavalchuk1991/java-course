package com.solvd.musicstreamingservice.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Mocked database connection with CRUD operations.
 */
public class Connection {

    private static final Logger LOGGER = LogManager.getLogger(Connection.class);

    private final int id;
    private boolean inUse;

    public Connection(int id) {
        this.id = id;
        this.inUse = false;
    }

    public void create(String entity) {
        LOGGER.info("[Connection-{}] CREATE: {}", id, entity);
    }

    public String get(int entityId) {
        LOGGER.info("[Connection-{}] GET entity with id: {}", id, entityId);
        return "Entity-" + entityId;
    }

    public void update(int entityId, String newValue) {
        LOGGER.info("[Connection-{}] UPDATE entity {}: {}", id, entityId, newValue);
    }

    public void delete(int entityId) {
        LOGGER.info("[Connection-{}] DELETE entity with id: {}", id, entityId);
    }

    public int getId() {
        return id;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    @Override
    public String toString() {
        return "Connection-" + id;
    }
}
