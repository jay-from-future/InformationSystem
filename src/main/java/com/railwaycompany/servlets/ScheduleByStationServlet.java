package com.railwaycompany.servlets;

import com.railwaycompany.entities.Station;
import com.railwaycompany.serviceBeans.ScheduleData;
import com.railwaycompany.services.abstractServices.ScheduleService;
import com.railwaycompany.services.abstractServices.ServiceFactory;
import com.railwaycompany.services.abstractServices.StationService;
import com.railwaycompany.services.servicesImpl.ScheduleServiceImpl;
import com.railwaycompany.services.servicesImpl.ServiceFactorySingleton;
import com.railwaycompany.utils.ValidationHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ScheduleByStationServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(ScheduleByStationServlet.class.getName());

    private ScheduleService scheduleService;
    private StationService stationService;

    @Override
    public void init() throws ServletException {
        ServiceFactory serviceFactory = ServiceFactorySingleton.getInstance();
        scheduleService = serviceFactory.getScheduleService();
        stationService = serviceFactory.getStationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("extendedSearchByStationForm", Boolean.valueOf(req.getParameter("extendedForm")));
        resp.sendRedirect("/#schedule_by_station");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String stationName = req.getParameter(ScheduleServiceImpl.STATION_NAME_ATTR);
        log.info(ScheduleServiceImpl.STATION_NAME_ATTR + " : " + stationName);

        if (ValidationHelper.isValidStationName(stationName)) {
            HttpSession session = req.getSession();
            session.setAttribute("stationName", stationName);
            Station station = stationService.getStation(stationName);
            List<ScheduleData> scheduleOfTrainsByStation = scheduleService.getSchedule(station);
            if (scheduleOfTrainsByStation != null) {
                session.setAttribute("scheduleList", scheduleOfTrainsByStation);
                session.setAttribute("stationNotFound", false);
            } else {
                session.setAttribute("scheduleList", null);
                session.setAttribute("stationNotFound", true);
            }
        }
        resp.sendRedirect("/#schedule_by_station");
    }
}
