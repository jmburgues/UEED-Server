package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name= "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name= "street")
    private String street;
    @Column(name="number")
    private Integer number;
    @OneToOne
    @JoinColumn(name="rateId")
    private Rate rate;
    @OneToOne
    @JoinColumn(name="addressId")
    private Meter meter;
}
