package edu.utn.UEEDServer.model;

import java.util.List;

public class Client {
    private Integer id;
    private User user;
    private List<Address> adresses;

    public Client(Integer id, User user, List<Address> adresses) {
        this.id = id;
        this.user = user;
        this.adresses = adresses;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Address> getAdresses() {
        return adresses;
    }

    public void setAdresses(List<Address> adresses) {
        this.adresses = adresses;
    }
}
