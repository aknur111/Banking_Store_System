package model;

public class OrderReceipt {
    private final double totalBefore;
    private final double totalCharged;
    private final String notes;

    public OrderReceipt(double totalBefore, double totalCharged, String notes) {
        this.totalBefore = totalBefore;
        this.totalCharged = totalCharged;
        this.notes = notes;
    }

    public double totalBefore(){
        return totalBefore;
    }
    public double totalCharged(){
        return totalCharged;
    }
    public String notes(){
        return notes;
    }
}
