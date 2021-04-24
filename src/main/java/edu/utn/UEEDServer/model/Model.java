package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name ="models")
public class Model {
    @Id
    @Column(name ="modelId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column (name="name")
    private String name;
    @OneToOne
    @JoinColumn(name="brandId")
    private Brand brand;
}
