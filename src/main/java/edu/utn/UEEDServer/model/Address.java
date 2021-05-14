package edu.utn.UEEDServer.model;

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
    @Column(name="addressId")
    private Integer addressId;
    @Column(name= "street")
    private String street;
    @Column(name="number")
    private Integer number;
    @OneToOne
    @JoinColumn(name="rateId",foreignKey = @ForeignKey(name= "FK_addresses_rates"))
    private Rate rate;
    @OneToOne
    @JoinColumn(name="meterId", columnDefinition = "VARCHAR(40) not null unique",foreignKey = @ForeignKey(name = "FK_addresses_meters"))
    private Meter meter;
}
