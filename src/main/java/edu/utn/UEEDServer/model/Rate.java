package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="rates")

public class Rate {
    @Id
    @Column(name="rateId")
    private Integer id;
    @Column(name ="category")
    private String category;
    @Column(name="kwPrice")
    private Float price;


}
