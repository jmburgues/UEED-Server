package edu.utn.UEEDServer.model;

public class Rate {
    private Integer id;
    private String category;
    private Float price;

    public Rate(Integer id, String category, Float price) {
        this.id = id;
        this.category = category;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
