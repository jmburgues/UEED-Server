package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="BRANDS")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brandId")
    private Integer id;
    @Column(name= "name")
    private String name;
//
//    @OneToMany(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "brandId")
//    private List<Model> modelList;
}
