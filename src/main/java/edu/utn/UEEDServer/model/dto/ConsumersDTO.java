package edu.utn.UEEDServer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumersDTO {
    Integer clientId;
    Float totalConsumption;
}
