package com.solvd.musicstreamingservice.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents user subscription plan
 */
public class Subscription {

    // Type of subscription (Basic / Premium)
    private String type;

    // Monthly price of subscription (BigDecimal for precise monetary values)
    private BigDecimal monthlyPrice;

    /**
     * Constructor to initialize subscription
     */
    public Subscription(String type, BigDecimal monthlyPrice) {
        this.type = type;
        this.monthlyPrice = monthlyPrice;
    }

    // -------- Getters --------

    public String getType() {
        return type;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    // -------- Setters --------

    public void setType(String type) {
        this.type = type;
    }

    public void setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    @Override
    public String toString() {
        return "Subscription{type='" + type + "', monthlyPrice=" + monthlyPrice + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(type, that.type) && Objects.equals(monthlyPrice, that.monthlyPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, monthlyPrice);
    }
}
