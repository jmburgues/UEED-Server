package edu.utn.UEEDServer.model;

import java.util.Random;

public class Model {

    private Integer id;
    private String name;
    private Brand brand;

    public Model()
    {
        this.id = new Random().nextInt(465);
        this.name = "la marca";
        this.brand = new Brand();

    }

    public Model(Integer id,String name)
    {
        this.id = id;
        this.name = name;
        this.brand = new Brand();
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return
                "\nid=" + id +
                "\n name='" + name + '\'' +
                "\n brand=" + brand
                ;
    }
}
