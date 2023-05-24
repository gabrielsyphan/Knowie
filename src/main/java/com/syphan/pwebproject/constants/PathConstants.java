package com.syphan.pwebproject.constants;

public class PathConstants {

    public static final String VERSION = "v1";

    public static final String BASE_PATH = "/api/" + VERSION;

    public static final String EXAMS = BASE_PATH + "/exams";

    public static final String QUESTIONS = BASE_PATH + "/questions";

    public static final String ANSWERS = BASE_PATH + "/answers";

    public static final String USERS = BASE_PATH + "/users";

    public static final String LOGIN = BASE_PATH + "/login";

    public static final String CLASSROOM = BASE_PATH + "/classrooms";

    public static final String JOIN_CLASSROOM = CLASSROOM + "/join";

    public static final String RESET_PASSWORD = BASE_PATH + "/reset-password";

    public static final String FORGOT_PASSWORD = BASE_PATH + "/forgot-password";
}
