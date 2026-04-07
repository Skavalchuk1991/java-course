package com.solvd.musicstreamingservice.model;

/**
 * Complex enum representing subscription tiers.
 * Has fields, constructor, methods, and a static block.
 */
public enum SubscriptionTier {
    FREE("Free", 0.00, 5, false),
    BASIC("Basic", 4.99, 50, false),
    PREMIUM("Premium", 9.99, 500, true),
    FAMILY("Family", 14.99, 1000, true);

    private final String displayName;
    private final double monthlyPrice;
    private final int maxDownloads;
    private final boolean offlineAccess;

    // Static block
    static {
        System.out.println("SubscriptionTier enum loaded. Tiers available: " + values().length);
    }

    // Constructor
    SubscriptionTier(String displayName, double monthlyPrice, int maxDownloads, boolean offlineAccess) {
        this.displayName = displayName;
        this.monthlyPrice = monthlyPrice;
        this.maxDownloads = maxDownloads;
        this.offlineAccess = offlineAccess;
    }

    // Methods
    public String getDisplayName() { return displayName; }
    public double getMonthlyPrice() { return monthlyPrice; }
    public int getMaxDownloads() { return maxDownloads; }
    public boolean hasOfflineAccess() { return offlineAccess; }

    public String describe() {
        return displayName + " — $" + monthlyPrice + "/mo, " + maxDownloads + " downloads"
                + (offlineAccess ? ", offline access" : "");
    }
}
