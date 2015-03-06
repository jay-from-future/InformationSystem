package com.railwaycompany.services.abstractServices;

public interface ServiceFactory {

    AuthenticationService getAuthenticationService();

    StationService getStationService();

    ScheduleService getScheduleService();

    TrainService getTrainService();

    UserService getUserService();

    TicketService getTicketService();

    void close();
}