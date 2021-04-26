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
    @Column(columnDefinition = "integer auto_increment")
    private Integer id;
    @Column(columnDefinition = "varchar(40) unique")
    private String category;
    @Column(name="kwPrice")
    private Float kwPrice;
}
