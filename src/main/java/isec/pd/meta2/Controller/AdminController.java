package isec.pd.meta2.Controller;

import isec.pd.meta2.Server.EventManager;
import isec.pd.meta2.Shared.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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
    public ResponseEntity<String> deleteEvent(@PathVariable("eventDesignation") String eventDesignation){
        return ResponseEntity.ok((EventManager.deleteEvent(eventDesignation)) ? Messages.OK.toString() : ErrorMessages.INVALID_EVENT_NAME.toString());
    }

    @PostMapping("/events/genCode")
    public ResponseEntity<String> generatePresenceCode(@RequestParam(value = "args", required = true, defaultValue = "") String args){

        String[] argsPresence = args.split(",");
        String[] times = EventManager.getTime(argsPresence[0]).split(",");
        Time timeBeginEvent = null;
        Time timeEndEvent = null;
        try {
            timeBeginEvent = new Time(times[0]);
            timeEndEvent = new Time(times[1]);
        } catch (ParseException e) {
            System.out.println("Error while parsing the dates of the event! " + e.getMessage());
        }
        Time timeAtual = new Time();
        Event event1 = new Event(argsPresence[0],argsPresence[1],timeBeginEvent,timeEndEvent);

        if(!EventManager.checkIfCodeAlreadyCreated(argsPresence[0]))
            return ResponseEntity.ok(EventManager.registerPresenceCode(event1, Integer.parseInt(argsPresence[1]), timeAtual));
        else{
            int code = EventManager.generateCode();
            return ResponseEntity.ok((!EventManager.updatePresenceCode(code, Integer.parseInt(argsPresence[1]), argsPresence[0])) ? String.valueOf(code) : ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString());
        }
    }

    @GetMapping("/events/presences/{eventDesignation}")
    public ResponseEntity<EventResult> getPresencesInEvent(){

        return ResponseEntity.ok(null);
    }

}
