package edu.utn.UEEDServer.persistence.mysql;



import edu.utn.UEEDServer.exception.InexistentObjectException;
import edu.utn.UEEDServer.model.Emeter;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.persistence.EmeterDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmeterMysqlDAO  implements EmeterDAO {

    Connection connection;
    ReadingMysqlDAO readingMysqlDAO;


    public EmeterMysqlDAO(Connection connection)
    {

        this.connection = connection;
        readingMysqlDAO = new ReadingMysqlDAO();
    }

    @Override
    public void save(Emeter emeter) {
        try
        {
            PreparedStatement ps = this.connection.prepareStatement
                    ("INSERT into emeters (serialNumber,modelId) values (?,?)");

            ps.setString(1,emeter.getSerialNumber());
            ps.setInt(2,emeter.getModel().getId());
            ps.execute();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }


    }

    public void read(Emeter emeter)
    {

        this.readingMysqlDAO.save(new Reading(),emeter.getSerialNumber());
    }

    @Override
    public void remove(Emeter o) {


    }

    @Override
    public Emeter getById(String serialNumber) {

        try{
         final PreparedStatement preparedStatement = this.connection.prepareStatement(

                 "SELECT * from emeters where serialNumber = ?");
         preparedStatement.setString(1,serialNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return createEmeter(resultSet);
            }
        }
        catch (SQLException sqlException) {

            throw new InexistentObjectException();
        }
        return null;
    }

    private Emeter createEmeter(ResultSet resultSet) throws SQLException {

        return new Emeter(resultSet.getString("serialNumber"));
    }

    @Override
    public List<Emeter>getAll() {
        return new ArrayList<>();
    }





}
