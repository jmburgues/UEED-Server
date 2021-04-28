package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name ="MODELS")
public class Model {
    @Id
    @Column(name ="modelId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column (name="name")
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brandId",foreignKey = @ForeignKey(name="models_brands"))
    private Brand brand;
}
