package edu.utn.UEEDServer.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="READINGS")
public class Reading {

    @Id
    @Column(name="readingId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="readDate")
    private LocalDateTime readDate;
    @Column(name = "totalKw")
    private float totalKw;
    @Column(name = "readingPrice")
    private float readingPrice;
    @OneToOne
    @JoinColumn(name="billId")//Maybe replace this object with just an Integer??
    private Bill bill;
}