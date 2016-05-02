package com.kulpin.project.travelcompanion.utilities;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrei on 08.04.2016.
 */
public class Constants {
    public static final long userId = 1;

    /*Controller URLs*/
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

    /*Intent*/
    public static class RequestCodes{
        public static final int JOURNEY_REQUEST = 1;
        public static final int EVENT_REQUEST = 2;

        public static int PICK_IMAGE_REQUEST = 5;
    }
    public static class Actions{
        public static final String EDIT_JOURNEY_ACTION = "com.kulpin.project.edit_journey";
        public static final String EDIT_EVENT_ACTION = "com.kulpin.project.edit_event";
    }

    /*Gallery*/
    public static final String PHOTOS_DIRECTORY = "TCPhotos";
    public static final List<String> FILE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");
    public static int GRID_PADDING = 8;
    public static int NUM_COLUMNS = 3;

    /*Database*/
    public static final String DBNAME = "tcDB";
    public static final int DBVERSION = 1;
    public static final String PHOTOSTABLE = "photos";
}
