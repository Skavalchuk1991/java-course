package com.solvd.musicstreamingservice.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

/**
 * Handles payment processing logic
 */
public class PaymentService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentService.class);

    // Name of payment provider (e.g., Stripe, PayPal)
    private String paymentProvider;

    // Total revenue collected by system (BigDecimal for precise values)
    private BigDecimal totalRevenue;

    /**
     * Constructor
     */
    public PaymentService(String paymentProvider) {
        this.paymentProvider = paymentProvider;
        this.totalRevenue = BigDecimal.ZERO;
    }

    /**
     * Business method:
     * Processes payment and increases total revenue
     */
    public void processPayment(BigDecimal amount) {
        totalRevenue = totalRevenue.add(amount);
        LOGGER.info("Payment of $ {} processed via {}", amount, paymentProvider);
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
