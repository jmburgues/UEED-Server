package edu.utn.UEEDServer.model;

import java.time.LocalDateTime;

public class Bill {
    private Integer billId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Integer initialConsumption;
    private Integer finalConsumption;
    private Integer totalConsumption;
    private String meterId; // just for info, not for linking to Meter object.
    private RateCategory rateCategory;
    private float ratePrice;
    private float totalPrice;
    private Integer clientId; // just for info, not for linking to Client object.

    // is this model ok??


    public Bill(LocalDateTime dateFrom, LocalDateTime dateTo, Integer initialConsumption, Integer finalConsumption, Integer totalConsumption, String meterId, RateCategory rateCategory, float ratePrice, float totalPrice, Integer clientId) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.initialConsumption = initialConsumption;
        this.finalConsumption = finalConsumption;
        this.totalConsumption = totalConsumption;
        this.meterId = meterId;
        this.rateCategory = rateCategory;
        this.ratePrice = ratePrice;
        this.totalPrice = totalPrice;
        this.clientId = clientId;
    }

    public Integer getBillId() {
        return billId;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getInitialConsumption() {
        return initialConsumption;
    }

    public void setInitialConsumption(Integer initialConsumption) {
        this.initialConsumption = initialConsumption;
    }

    public Integer getFinalConsumption() {
        return finalConsumption;
    }

    public void setFinalConsumption(Integer finalConsumption) {
        this.finalConsumption = finalConsumption;
    }

    public Integer getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(Integer totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public RateCategory getRateCategory() {
        return rateCategory;
    }

    public void setRateCategory(RateCategory rateCategory) {
        this.rateCategory = rateCategory;
    }

    public float getRatePrice() {
        return ratePrice;
    }

    public void setRatePrice(float ratePrice) {
        this.ratePrice = ratePrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
}
