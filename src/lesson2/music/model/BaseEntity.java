package lesson2.music.model;

import java.util.Objects;

/**
 * Abstract base class for all domain entities.
 * Provides common id field and enforces toString() implementation.
 */
public abstract class BaseEntity {

    protected int id;

    public BaseEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Force all entities to implement their own string representation
    @Override
    public abstract String toString();
}
