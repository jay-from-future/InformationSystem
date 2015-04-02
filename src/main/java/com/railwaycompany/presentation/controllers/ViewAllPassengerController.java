package com.railwaycompany.presentation.controllers;

import com.railwaycompany.business.dto.PassengerData;
import com.railwaycompany.business.services.interfaces.PassengerService;
import com.railwaycompany.utils.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("view_all_passengers")
public class ViewAllPassengerController {

    private static final String HIDE_ALL_PASSENGERS_PARAM = "hideAllPassengers";
    private static final String TRAIN_ID_PARAM = "trainId";
    private static final String ALL_PASSENGER_LIST_ATTR = "allPassengerList";
    private static final String PASSENGER_NOT_FOUND_ATTR = "passengersNotFound";
    private static final String PASSENGER_NOT_FOUND_TRAIN_ID_ATTR = "passengersNotFoundTrainId";
    private static final String INVALID_TRAIN_ID_ATTR = "invalidTrainIdError";

    private static final Logger LOG = Logger.getLogger(ViewAllPassengerController.class.getName());

    @Autowired
    private PassengerService passengerService;

    @RequestMapping(method = RequestMethod.GET)
    public String doGet(HttpServletRequest req) {
        if (Boolean.valueOf(req.getParameter(HIDE_ALL_PASSENGERS_PARAM))) {
            req.getSession().removeAttribute(ALL_PASSENGER_LIST_ATTR);
        }
        return "employee_page";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doPost(HttpServletRequest req) {
        HttpSession session = req.getSession();
        String trainIdStr = req.getParameter(TRAIN_ID_PARAM);
        List<PassengerData> allPassengersList = null;
        if (ValidationHelper.isValidId(trainIdStr)) {
            int trainId = Integer.valueOf(trainIdStr);
            allPassengersList = passengerService.getAllPassengersByTrainId(trainId);
            if (allPassengersList == null || allPassengersList.isEmpty()) {
                session.setAttribute(PASSENGER_NOT_FOUND_ATTR, true);
                session.setAttribute(PASSENGER_NOT_FOUND_TRAIN_ID_ATTR, trainId);
            }
        } else {
            LOG.info("Invalid trainId: " + trainIdStr);
            session.setAttribute(INVALID_TRAIN_ID_ATTR, true);
        }
        session.setAttribute(ALL_PASSENGER_LIST_ATTR, allPassengersList);
        return "employee_page";
    }
}