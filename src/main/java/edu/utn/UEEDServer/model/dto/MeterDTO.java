package edu.utn.UEEDServer.model.dto;

import edu.utn.UEEDServer.model.Address;
import edu.utn.UEEDServer.model.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeterDTO {
    private String serialNumber;
    private String password;
    private Integer modelId;
    private Integer addressId;
}
