package com.solution.green.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GreenErrorCode {

    NO_MEMBER("There is no corresponding MEMBER."),
    WRONG_PASSWORD("Invalid password."),
    ALREADY_REGISTERED("This member is already registered."),

    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.")

    // TODO - not yet
//    CREATE_FAIL("Failed to create entity on request."),
//    CREATE_EVENT_FAIL(500, "CREATE-ERR-500", "Failed to create event entity on request."),
//    CREATE_PARTICIPANT_FAIL(500, "CREATE-ERR-500", "Failed to create participant entity on request."),
//
//    DELETE_FAIL(500, "DELETE-ERR-500", "Failed to delete entity on request."),
//
//    INVALID_DELETE_SUPERUSER(500, "INVALID-ERR-500", "SuperUser cannot be deleted from Travel."),
//    INVALID_DELETE_NOTSUPERUSER(500, "INVALID-ERR-500", "Only SuperUser can delete the Travel."),
//    INVALID_DELETE_EVENTEXISTED(500, "INVALID-ERR-500", "This Person has participated Events."),
//
//    INVALID_DELETE_TRAVELEXISTED(500, "INVALID-ERR-500", "This Person has participated Travel.")
    ;

    private String message;
}
