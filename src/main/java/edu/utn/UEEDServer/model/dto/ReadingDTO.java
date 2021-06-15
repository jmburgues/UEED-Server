package edu.utn.UEEDServer.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadingDTO {
    @JsonProperty("date")
    private LocalDateTime readDate;
    @JsonProperty("value")
    private Float totalKw;
    @JsonProperty("serialNumber")
    private String meterSerialNumber;
    @JsonProperty("password")
    private String password;
}