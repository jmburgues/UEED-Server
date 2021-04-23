package edu.utn.UEEDServer.repository;

import java.util.List;

public interface DAO<OBJ,ID> {

    void save(OBJ O);
    void remove(OBJ o);
    OBJ getById(ID id);

    List<OBJ> getAll();
}
