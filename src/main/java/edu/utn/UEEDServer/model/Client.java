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
@Table(name = "CLIENTS")
public class Client {

    @Id
    @Column(name="clientId")
    private Integer id;
    @OneToMany
    @JoinColumn(name = "clientId")
    private List<Address> addresses;
    @OneToMany // on OneToMany JoinColumn creates fk column on destiny table
    @JoinColumn(name = "clientId")
    private List<Bill> bills;
    @OneToOne // on OneToOne JoinColumn creates fk column on source table
    @JoinColumn(name="username")
    private User user;
}
