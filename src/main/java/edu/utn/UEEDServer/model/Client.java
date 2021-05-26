package edu.utn.UEEDServer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name = "CLIENTS")
public class Client {

    @Id
    @Column(name="clientId")
    private Integer id;
    @JsonManagedReference
    @OneToMany(mappedBy="client", targetEntity = Address.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Address> addresses;
    @OneToMany // on OneToMany JoinColumn creates fk column on destiny table
    @JoinColumn(name = "clientId",foreignKey = @ForeignKey(name="FK_clients_bills"))
    private List<Bill> bills;
    @OneToOne // on OneToOne JoinColumn creates fk column on source table
    @JoinColumn(name="username",foreignKey = @ForeignKey(name="FK_clients_users"))
    private User user;
}
