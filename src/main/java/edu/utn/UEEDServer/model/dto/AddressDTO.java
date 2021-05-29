package edu.utn.UEEDServer.model.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
