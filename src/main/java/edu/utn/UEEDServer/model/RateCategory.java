package edu.utn.UEEDServer.model;

// should we persist this Enum into DB ??
public enum RateCategory {
    RESIDENTIAL(1,"RESIDENTIAL",40),
    INDUSTRIAL(2,"INDUSTRIAL",55),
    GENERAL(3,"GENERAL",60);

    private Integer id;
    private String description;
    private Integer value;

    RateCategory(Integer id, String description, Integer value) {
        this.id = id;
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public Integer getValue() {
        return value;
    }
}
