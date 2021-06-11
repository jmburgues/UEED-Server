package edu.utn.UEEDServer.model.dto;

import javax.validation.constraints.NotNull;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    @NotNull
    private String street;
    @NotNull
    private Integer number;
    @NotNull
    private Integer rateId;
    @NotNull
    private Integer clientId;
}
