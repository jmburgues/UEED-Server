package edu.utn.UEEDServer.persistence.mysql;



import edu.utn.UEEDServer.model.Meter;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.persistence.ReadingDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadingMysqlDAO implements ReadingDAO {

    Connection connection;


    @Override
    public void save(Reading reading, String emeterId) {

        try {
            PreparedStatement ps = this.connection.prepareStatement
                    ("INSERT  into readings (emeter_id,reading,dateTime,billId) values(?,?,?,?)");
            ps.setString(1, emeterId);
            ps.setFloat(2, reading.getReading());
            ps.setDate(3, (java.sql.Date) reading.getDatetime());
            ps.setInt(4, 0);

            ps.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    @Override
    public void save(Reading O) {



    }

    @Override
    public void remove(Reading reading) {

        try{
            PreparedStatement ps = this.connection.prepareStatement(
                    "DELETE from readings where readingId = ?"   );

            ps.setInt(1,reading.getId());
            ps.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    @Override
    public Reading getById(Integer readingId) {
        Reading reading = null;
        try{
            PreparedStatement ps = this.connection.prepareStatement(
                    "SELECT * from readings where id = ?");
            ps.setInt(1,readingId);
            ResultSet rs =ps.executeQuery();
            while(rs.next())
            {
               reading = createReading(rs);
            }
        }

        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return reading;
    }

    public List<Reading>getByEmeter(Meter meter)
    {
        try
        {
            PreparedStatement ps = this.connection.prepareStatement(
                    "Select * from readings where emeterId = ?"
            );
            ps.setString(1, meter.getSerialNumber());
            ResultSet rs=ps.executeQuery();
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    return  new ArrayList<>();
    }

    @Override
    public List<Reading> getAll() {
        return null;
    }

    @Override
    public List<Reading> getByDates(Date from, Date to) {



        return null;
    }

    public Reading createReading(ResultSet rs) throws SQLException {
        Reading reading = null;
        while(rs.next()){
              reading = new Reading(
                      rs.getInt("id"),
                      rs.getFloat("reading"),
                      rs.getDate("dateTime"),
                      rs.getBoolean("billed")

        );}
        return reading;
    }


}
