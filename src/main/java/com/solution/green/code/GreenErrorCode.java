package com.solution.green.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GreenErrorCode {
    ALREADY_EXISTED(500, "CREATE-PERSON-ERR-500","have already been invited."),
    ALREADY_REGISTERED(500, "CREATE-USER-ERR-500","is already registered."),

    WRONG_PASSWORD(500, "LOGIN-ERR-500","Invalid password."),
    WRONG_EMAIL(500,"LOGIN-ERR-500","There is no such email information."),

    INVALID_DELETE_SUPERUSER(500, "INVALID-ERR-500", "SuperUser cannot be deleted from Travel."),
    INVALID_DELETE_NOTSUPERUSER(500, "INVALID-ERR-500", "Only SuperUser can delete the Travel."),
    INVALID_DELETE_EVENT_EXISTED(500, "INVALID-ERR-500", "This Person has participated Events."),
    INVALID_DELETE_TRAVEL_EXISTED(500, "INVALID-ERR-500",
            "This Person has participated Travel. \n" +
                    "Please withdraw from the trip to deregister."),

    NO_MEMBER(500, "MEMBER-ERR-500", "There is no corresponding MEMBER."),
    NO_CATEGORY(500, "CATEGORY-ERR-500", "There is no corresponding CATEGORY."),
    NO_QUEST(500, "QUEST-ERR-500", "There is no corresponding QUEST."),
    NO_MANAGER(500, "EVENT-ERR-500","There is no MANAGER for the event."),
    NO_SUPERUSER(500, "TRAVEL-SUPERUSER-ERROR", "There is no SuperUser on this trip."),
    NO_TRAVEL(500, "TRAVEL-ERR-500","There is no corresponding TRAVEL."),
    NO_EVENT(500, "EVENT-ERR-500","There is no corresponding EVENT."),

    CREATE_FAIL(500, "CREATE-ERR-500", "Failed to create entity on request."),

    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),




    ;
    private int status;
    private String errorCode;
    private String message;
}
