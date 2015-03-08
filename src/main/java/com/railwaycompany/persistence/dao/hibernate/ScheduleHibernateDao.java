package com.railwaycompany.persistence.dao.hibernate;

import com.railwaycompany.persistence.dao.interfaces.ScheduleDao;
import com.railwaycompany.persistence.entities.Schedule;
import com.railwaycompany.persistence.entities.Train;
import com.railwaycompany.utils.DateHelper;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleHibernateDao extends HibernateDao<Schedule> implements ScheduleDao {

    private static final String SCHEDULES_WITH_STATION_ID = "SELECT s FROM Schedule s WHERE s.station.id = :stationId";
    private static final String SCHEDULES_STATION_DEPARTURE = "SELECT s FROM Schedule s WHERE s" + ".station" +
            ".id = :stationId AND s.timeDeparture >= :departureDate";
    private static final String SCHEDULES_STATION_ARRIVAL = "SELECT s FROM Schedule s WHERE s" + ".station" +
            ".id = :stationId AND s.timeArrival <= :arrivalDate";
    private static final String SCHEDULES_WITH_STATION_AND_TRAIN_ID = "SELECT s FROM Schedule s WHERE s.station.id = " +
            "" + ":stationId AND s.train.id = :trainId";

    private static final String SCHEDULES_WITH_STATION_AND_DATES = "SELECT s FROM Schedule s WHERE" +
            " s.station.id = :stationId AND s.timeDeparture >= :departureDateFrom AND s.timeDeparture <= :departureDateTo";

    private static final String SCHEDULES_BY_TRAIN_ID = "SELECT s.timeDeparture FROM Schedule s WHERE" +
            " s.train.id = :trainId AND s.station.id = :stationId";

    /**
     * Logger for ScheduleHibernateDao class.
     */
    private static Logger log = Logger.getLogger(ScheduleHibernateDao.class.getName());

    /**
     * HibernateDao constructor.
     *
     * @param entityManager - entity manager
     */
    public ScheduleHibernateDao(EntityManager entityManager) {
        super(entityManager, Schedule.class);
    }

    @Override
    public List<Schedule> getSchedules(int stationId) {

        Query query = entityManager.createQuery(SCHEDULES_WITH_STATION_ID);

        query.setParameter("stationId", stationId);

        List<Schedule> schedules = null;
        try {

            List resultList = query.getResultList();
            schedules = new ArrayList<>();
            for (Object s : resultList) {
                schedules.add((Schedule) s);
            }

        } catch (NoResultException e) {
            log.log(Level.INFO, "No schedule was found for stationId = \"" + stationId + "\"");
        }

        return schedules;
    }

    @Override
    public List<Schedule> getSchedules(int stationId, Date departureDate) {
        Query query = entityManager.createQuery(SCHEDULES_STATION_DEPARTURE);
        query.setParameter("stationId", stationId);
        query.setParameter("departureDate", departureDate);

        List<Schedule> schedules = null;
        try {
            List resultList = query.getResultList();
            schedules = new ArrayList<>();
            for (Object s : resultList) {
                schedules.add((Schedule) s);
            }
        } catch (NoResultException e) {
            log.log(Level.INFO, "No schedule was found for stationId: \"" + stationId + "\", departure date: \"" +
                    departureDate + "\"");
        }
        return schedules;
    }

    @Override
    public List<Schedule> getSchedules(Date arrivalDate, int stationId) {
        Query query = entityManager.createQuery(SCHEDULES_STATION_ARRIVAL);
        query.setParameter("arrivalDate", arrivalDate);
        query.setParameter("stationId", stationId);

        List<Schedule> schedules = null;
        try {
            List resultList = query.getResultList();
            if (!resultList.isEmpty()) {
                schedules = new ArrayList<>();
                for (Object s : resultList) {
                    schedules.add((Schedule) s);
                }
            }
        } catch (NoResultException e) {
            log.log(Level.INFO, "No schedule was found for stationId: \"" + stationId + "\", arrival date: \"" +
                    arrivalDate + "\"");
        }
        return schedules;
    }

    @Override
    public List<Schedule> getSchedules(int stationId, int trainId) {

        Query query = entityManager.createQuery(SCHEDULES_WITH_STATION_AND_TRAIN_ID);

        query.setParameter("stationId", stationId);
        query.setParameter("trainId", trainId);

        List<Schedule> schedules = null;
        try {
            List resultList = query.getResultList();
            schedules = new ArrayList<>();
            for (Object s : resultList) {
                schedules.add((Schedule) s);
            }
        } catch (NoResultException e) {
            log.log(Level.INFO, "No schedule was found for stationId: \"" + stationId + "\", trainId: \"" +
                    trainId + "\"");
        }
        return schedules;
    }

    @Override
    public Integer getTrainId(int trainNumber, int stationId, Date departureDate) {
        Integer trainId = null;
        Date departureDateTo = new Date(departureDate.getTime() + DateHelper.MILLIS_IN_DAY);
        List<Schedule> schedules = getSchedules(stationId, departureDate, departureDateTo);
        for (Schedule s : schedules) {
            Train train = s.getTrain();
            if (train.getNumber() == trainNumber) {
                trainId = train.getId();
            }
        }
        return trainId;
    }

    @Override
    public List<Schedule> getSchedules(int stationId, Date departureDateFrom, Date departureDateTo) {

        Query query = entityManager.createQuery(SCHEDULES_WITH_STATION_AND_DATES);

        query.setParameter("stationId", stationId);
        query.setParameter("departureDateFrom", departureDateFrom);
        query.setParameter("departureDateTo", departureDateTo);

        List<Schedule> schedules = null;
        try {
            List resultList = query.getResultList();
            schedules = new ArrayList<>();
            for (Object s : resultList) {
                schedules.add((Schedule) s);
            }
        } catch (NoResultException e) {
//            log.log(Level.INFO, "No schedule was found for stationId: \"" + stationId + "\", trainId: \"" +
//                    trainId + "\"");
        }
        return schedules;
    }

    @Override
    public Date getDepartureDate(int trainId, int stationId) {

        Query query = entityManager.createQuery(SCHEDULES_BY_TRAIN_ID);
        query.setParameter("trainId", trainId);
        query.setParameter("stationId", stationId);

        Date date = null;
        try {
            date = (Date) query.getSingleResult();
        } catch (Exception e) {
        }

        return date;
    }
}