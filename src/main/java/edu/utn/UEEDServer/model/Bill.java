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
    private
}
