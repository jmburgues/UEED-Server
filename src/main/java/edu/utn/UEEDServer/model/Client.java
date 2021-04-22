package edu.utn.UEEDServer.model;

import java.util.List;

public class Client {
    private Integer id;
    private User user;
    private List<Address> adresses;
    private List<Bill> billings;

    public Client(User user) {
        this.user = user;
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

    public List<Bill> getBillings() {
        return billings;
    }

    public void setBillings(List<Bill> billings) {
        this.billings = billings;
    }
}
