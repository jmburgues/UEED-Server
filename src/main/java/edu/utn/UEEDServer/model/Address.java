package edu.utn.UEEDServer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name= "ADDRESSES")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressId;
    private String street;
    private Integer number;
    @OneToOne
    @JoinColumn(name="rateId",foreignKey = @ForeignKey(name= "FK_addresses_rates"))
    private Rate rate;
    @JsonBackReference
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId")
    private Client client;
}
