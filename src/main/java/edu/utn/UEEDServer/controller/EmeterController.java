package edu.utn.UEEDServer.controller;



import edu.utn.UEEDServer.model.Emeter;
import edu.utn.UEEDServer.persistence.mysql.EmeterMysqlDAO;


import java.util.List;

public class EmeterController {

    private EmeterMysqlDAO emeterMysqlDAO;



    public List<Emeter> getAll()
    {
        return emeterMysqlDAO.getAll();
    }

    public Emeter getById(String serialNumber)
    {
        return emeterMysqlDAO.getById(serialNumber);
    }







}
