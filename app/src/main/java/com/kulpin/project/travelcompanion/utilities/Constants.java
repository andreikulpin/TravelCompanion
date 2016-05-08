package com.kulpin.project.travelcompanion.utilities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Andrei on 08.04.2016.
 */
public class Constants {
    public static final long userId = 1;

    /*Controller URLs*/
    public static class URL {
        private static final String HOST = "http://tcs-kulpin.rhcloud.com/";

        public static final String CREATE_ACCOUNT = HOST + "users/newUser";
        public static final String GET_USER_BY_USERNAME = HOST + "users/userByUsername/";
        public static final String GET_USER_BY_EMAIL = HOST + "users/userByEmail/";
        public static final String LOGIN_BY_USERNAME = HOST + "users/loginByUsername";
        public static final String LOGIN_BY_EMAIL = HOST + "users/loginByEmail";

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

    public static class HttpStatus{
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int CONFLICT = 409;
    }

    public static class HttpMessageCodes{
        public static final int USER_CREATED = 0;
        public static final int USERNAME_EXISTS = 1;
        public static final int EMAIL_EXISTS = 2;
        public static final int USERNAME_NOT_FOUND = 3;
        public static final int EMAIL_NOT_FOUND = 4;
        public static final int INCORRECT_PASSWORD = 5;
    }

    /*Intent*/
    public static class RequestCodes{
        public static final int JOURNEY_REQUEST = 1;
        public static final int EVENT_REQUEST = 2;

        public static int PICK_IMAGE_REQUEST = 3;
        public static int PICK_FILE_REQUEST = 4;

        public static int SIGNUP_REQUEST = 5;
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
