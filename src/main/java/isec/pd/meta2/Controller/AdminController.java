package isec.pd.meta2.Controller;

import isec.pd.meta2.Shared.ErrorMessages;
import isec.pd.meta2.Shared.EventResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/events?timeBegin=&timeEnd=&eventName=")
    public ResponseEntity<EventResult> getAllEvents(){

        return ResponseEntity.ok(null);
    }

    @PostMapping("/events/newEvent")
    public ResponseEntity<String> createEvent(){

        return ResponseEntity.ok(ErrorMessages.CREATE_EVENT_FAILED.toString());
    }

    @DeleteMapping("/events/delete/{eventDesignation}")
    public ResponseEntity<String> deleteEvent(){

        return ResponseEntity.ok(ErrorMessages.INVALID_EVENT_NAME.toString());
    }

    @PostMapping("/events/genCode")
    public ResponseEntity<String> generatePresenceCode(){

        return ResponseEntity.ok(ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString());
    }

    @GetMapping("/events/presences/{eventDesignation}")
    public ResponseEntity<EventResult> getPresencesInEvent(){

        return ResponseEntity.ok(null);
    }

}
