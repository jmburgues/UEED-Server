package edu.utn.UEEDServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="USERS")
public class User {

    @Id
    @Column(name="username")
    private String username;
    private String password;
    private String name;
    private String surname;
    @Column(columnDefinition = "BIT default 0")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean employee;
}
