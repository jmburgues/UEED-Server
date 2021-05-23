package edu.utn.UEEDServer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadingDTO {
    private LocalDateTime readDate;
    private Float totalKw;
    private String meterSerialNumber;
    private String password;
}