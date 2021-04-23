package edu.utn.UEEDServer.repository.mysql;



import edu.utn.UEEDServer.exception.InexistentObjectException;
import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.repository.meterDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class meterMysqlDAO implements meterDAO {

    Connection connection;
    ReadingMysqlDAO readingMysqlDAO;


    public meterMysqlDAO(Connection connection)
    {

        this.connection = connection;
        readingMysqlDAO = new ReadingMysqlDAO();
    }

    @Override
    public void save(Meter meter) {
        try
        {
            PreparedStatement ps = this.connection.prepareStatement
                    ("INSERT into METERS (serialNumber,modelId) values (?,?)");

            ps.setString(1, meter.getSerialNumber());
            ps.setInt(2, meter.getModel().getId());
            ps.execute();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }


    }

    public void read(Meter meter)
    {

        this.readingMysqlDAO.save(new Reading(), meter.getSerialNumber());
    }

    @Override
    public void remove(Meter o) {


    }

    @Override
    public Meter getById(String serialNumber) {

        try{
         final PreparedStatement preparedStatement = this.connection.prepareStatement(

                 "SELECT * from METERS where serialNumber = ?");
         preparedStatement.setString(1,serialNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return createMmeter(resultSet);
            }
        }
        catch (SQLException sqlException) {

            throw new InexistentObjectException();
        }
        return null;
    }



    @Override
    public List<Meter>getAll() {
        return new ArrayList<>();
    }





}
