package com.example.kalkulatorrealizacjiprodukcji;

public class Efficiency {

    private Double cycleTime;
    private Integer quantity;
    private Double multiply;
    private Integer selectedCavityNumber;
    private Double grams;

    public Efficiency() {
    }

    public Efficiency(Double cycleTime, Integer quantity, Double multiply, Integer selectedCavityNumber, Double grams) {
        this.cycleTime = cycleTime;
        this.quantity = quantity;
        this.multiply = multiply;
        this.selectedCavityNumber = selectedCavityNumber;
        this.grams = grams;
    }

    public Double getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(Double cycleTime) {
        this.cycleTime = cycleTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getMultiply() {
        return multiply;
    }

    public void setMultiply(Double multiply) {
        this.multiply = multiply;
    }

    public Integer getSelectedCavityNumber() {
        return selectedCavityNumber;
    }

    public void setSelectedCavityNumber(Integer selectedCavityNumber) {
        this.selectedCavityNumber = selectedCavityNumber;
    }

    public Double getGrams() {
        return grams;
    }

    public void setGrams(Double grams) {
        this.grams = grams;
    }
}
