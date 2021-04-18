package edu.utn.UEEDServer.controller;



import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.persistence.mysql.EmeterMysqlDAO;


import java.util.List;

public class EmeterController {

    private EmeterMysqlDAO emeterMysqlDAO;



    public List<Meter> getAll()
    {
        return emeterMysqlDAO.getAll();
    }

    public Meter getById(String serialNumber)
    {
        return emeterMysqlDAO.getById(serialNumber);
    }







}
