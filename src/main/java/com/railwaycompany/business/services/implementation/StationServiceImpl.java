package com.railwaycompany.business.services.implementation;

import com.railwaycompany.persistence.dao.interfaces.DaoContext;
import com.railwaycompany.persistence.dao.interfaces.StationDao;
import com.railwaycompany.persistence.entities.Station;
import com.railwaycompany.business.services.interfaces.StationService;

import java.util.logging.Logger;

public class StationServiceImpl implements StationService {

    /**
     * Logger for StationServiceImpl class.
     */
    private static Logger log = Logger.getLogger(StationServiceImpl.class.getName());

    private StationDao stationDao;

    public StationServiceImpl(DaoContext daoContext) {
        stationDao = (StationDao) daoContext.get(StationDao.class);
    }

    @Override
    public Station getStation(String name) {
        return stationDao.getStation(name);
    }
}