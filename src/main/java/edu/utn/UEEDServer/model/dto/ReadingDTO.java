package edu.utn.UEEDServer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadingDTO {
    private String meterSerialNumber;
    private float totalKw;
    private String readDate;
    private String password;
}
