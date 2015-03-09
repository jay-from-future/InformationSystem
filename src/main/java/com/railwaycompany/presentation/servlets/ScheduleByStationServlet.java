package com.railwaycompany.presentation.servlets;

import com.railwaycompany.business.dto.ScheduleData;
import com.railwaycompany.business.services.implementation.ScheduleServiceImpl;
import com.railwaycompany.business.services.implementation.ServiceFactorySingleton;
import com.railwaycompany.business.services.interfaces.ScheduleService;
import com.railwaycompany.business.services.interfaces.ServiceFactory;
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

    @Override
    public void init() throws ServletException {
        ServiceFactory serviceFactory = ServiceFactorySingleton.getInstance();
        scheduleService = serviceFactory.getScheduleService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("extendedSearchByStationForm", Boolean.valueOf(req.getParameter("extendedForm")));
        getServletContext().getRequestDispatcher("/index").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String stationName = req.getParameter(ScheduleServiceImpl.STATION_NAME_ATTR);
        log.info(ScheduleServiceImpl.STATION_NAME_ATTR + " : " + stationName);

        if (ValidationHelper.isValidStationName(stationName)) {
            HttpSession session = req.getSession();
            session.setAttribute("stationName", stationName);
            List<ScheduleData> scheduleOfTrainsByStation = scheduleService.getSchedule(stationName);
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
