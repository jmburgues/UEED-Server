package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

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
    @JoinColumn(name="rateId",foreignKey = @ForeignKey(name= "addresses_rates"))
    private Rate rate;
    @OneToOne
    @JoinColumn(name="meterId", columnDefinition = "BINARY(16) not null unique",foreignKey = @ForeignKey(name = "addresses_meters"))
    private Meter meter;
}
