package edu.utn.UEEDServer.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="READINGS")
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer readingId;
    private LocalDateTime readDate;
    private Float totalKw;
    private String meterSerialNumber;
    private Float readingPrice;
    @OneToOne
    @JoinColumn(name="billId")//Maybe replace this object with just an Integer??
    private Bill bill;
}