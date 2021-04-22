package edu.utn.UEEDServer.controller;



import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.persistence.mysql.meterMysqlDAO;


import java.util.List;

public class EmeterController {

    private meterMysqlDAO emeterMysqlDAO;



    public List<Meter> getAll()
    {
        return emeterMysqlDAO.getAll();
    }

    public Meter getById(String serialNumber)
    {
        return emeterMysqlDAO.getById(serialNumber);
    }







}
