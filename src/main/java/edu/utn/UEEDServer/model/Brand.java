package edu.utn.UEEDServer.model;

public class Brand {

    private Integer id;
    private String name;

    public Brand() {
    }

    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "\nid=" + id +
                "\n name='" + name + '\''
                ;
    }
}
