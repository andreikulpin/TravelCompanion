package com.kulpin.project.travelcompanion;

/**
 * Created by Andrei on 08.04.2016.
 */
public class Constants {
    public static class URL {
        private static final String HOST = "http://tcs-kulpin.rhcloud.com/";

        public static final String GET_ALL_EVENTS = HOST + "events/all/";
        public static final String GET_EVENT = HOST + "events/eventById/";
        public static final String ADD_EVENT = HOST + "events/newEvent";
        public static final String DELETE_EVENT = HOST + "events/delete/";

        public static final String GET_ALL_JOURNEYS = HOST + "journeys/all/";
        public static final String GET_ACTIVE_JOURNEYS = HOST + "journeys/active/";
        public static final String GET_LAST_JOURNEYS = HOST + "journeys/last/";
        public static final String GET_JOURNEY = HOST + "journeys/eventById/";
        public static final String ADD_JOURNEY = HOST + "journeys/newJourney";
        public static final String DELETE_JOURNEY = HOST + "journeys/delete/";
    }
}
