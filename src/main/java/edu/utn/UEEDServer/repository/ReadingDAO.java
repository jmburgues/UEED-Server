package edu.utn.UEEDServer.repository;


import edu.utn.UEEDServer.model.Reading;

import java.util.Date;
import java.util.List;

public interface ReadingDAO extends DAO<Reading,Integer>{

    List<Reading> getByDates(Date from, Date to);

    void save(Reading reading,String emeterId);
}
