package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name="RATES")
public class Rate {
    @Id
    @Column(name="rateId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="category",columnDefinition = "varchar(40) unique")
    private String category;
    @Column(name="kwPrice")
    private Float kwPrice;
}
