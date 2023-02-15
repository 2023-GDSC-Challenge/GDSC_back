package com.solution.green.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_MEMBER(500, "MEMBER-ERR-500", "There is no corresponding MEMBER."),
    WRONG_PASSWORD(500, "LOGIN-ERR-500","Invalid password."),
    ALREADY_REGISTERED(500, "CREATE-USER-ERR-500","is already registered."),

    // TODO - not yet
    CREATE_FAIL(500, "CREATE-ERR-500", "Failed to create entity on request."),
    CREATE_EVENT_FAIL(500, "CREATE-ERR-500", "Failed to create event entity on request."),
    CREATE_PARTICIPANT_FAIL(500, "CREATE-ERR-500", "Failed to create participant entity on request."),

    DELETE_FAIL(500, "DELETE-ERR-500", "Failed to delete entity on request."),

    INVALID_DELETE_SUPERUSER(500, "INVALID-ERR-500", "SuperUser cannot be deleted from Travel."),
    INVALID_DELETE_NOTSUPERUSER(500, "INVALID-ERR-500", "Only SuperUser can delete the Travel."),
    INVALID_DELETE_EVENTEXISTED(500, "INVALID-ERR-500", "This Person has participated Events."),

    INVALID_DELETE_TRAVELEXISTED(500, "INVALID-ERR-500", "This Person has participated Travel.");


    private int status;
    private String errorCode;
    private String message;
}
