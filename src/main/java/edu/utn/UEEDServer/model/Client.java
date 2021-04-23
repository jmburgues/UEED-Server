package edu.utn.UEEDServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
public class Client {

    @Id
    @Column(name="clientId" )
    private Integer id;
    @OneToOne
    @JoinColumn(name = "username")
    private User user;
    @OneToMany
    private List<Address> adresses;
    @OneToMany
    private List<Bill> billings;


}
