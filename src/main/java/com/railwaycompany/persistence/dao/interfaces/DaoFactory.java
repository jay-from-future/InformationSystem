package com.railwaycompany.persistence.dao.interfaces;

import javax.persistence.EntityManager;

/**
 * DaoFactory interface. Creates Factory objects for work with the database.
 */
public interface DaoFactory {

    EntityManager getEntityManager();

    /**
     * Returns Dao for working with User entities.
     *
     * @return UserDao object.
     */
    UserDao getUserDao();

    /**
     * Returns Dao for working with Station entities.
     *
     * @return StationDao object.
     */
    StationDao getStationDao();

    /**
     * Returns Dao for working with Schedule entities.
     *
     * @return ScheduleDao object.
     */
    ScheduleDao getScheduleDao();

    /**
     * Returns Dao for working with Train entities.
     *
     * @return TrainDao object.
     */
    TrainDao getTrainDao();

    /**
     * Returns Dao for working with Ticket entities.
     *
     * @return TicketDao object.
     */
    TicketDao getTicketDao();

    PassengerDao getPassengerDao();

    /**
     * Closes all object (Streams and e.g.) that should will be closed. Call this before object destroying.
     */
    void close();
}