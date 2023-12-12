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
        EventResult eventResult = EventManager.queryEvents(null,null);

        return eventResult != null ? ResponseEntity.ok(eventResult) : ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/events/newEvent")
    public ResponseEntity<String> createEvent(@RequestParam(value = "args", required = true, defaultValue = "") String args){
        String[] arguments = args.split(",");
        Time timeBegin = null;
        Time timeEnd = null;
        try{
            timeBegin = new Time(arguments[2]);
            timeEnd = new Time(arguments[3]);
        }catch (ParseException e){
            System.out.println("Error while parsing the dates of the event! " + e.getMessage());
        }
        Event event = new Event(arguments[0], arguments[1], timeBegin, timeEnd);
        return (EventManager.createEvent(event)) ? ResponseEntity.ok(Messages.OK.toString()) : ResponseEntity.badRequest().body(ErrorMessages.CREATE_EVENT_FAILED.toString());
    }

    @DeleteMapping("/events/delete/{eventDesignation}")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventDesignation") String eventDesignation){
        return (EventManager.deleteEvent(eventDesignation)) ? ResponseEntity.ok(Messages.OK.toString()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.INVALID_EVENT_NAME.toString());
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
            return EventManager.registerPresenceCode(event1, Integer.parseInt(argsPresence[1]), timeAtual).equals(Messages.OK.toString()) ? ResponseEntity.ok(Messages.OK.toString()) : ResponseEntity.badRequest().body(ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString());
        else{
            int code = EventManager.generateCode();
            return (!EventManager.updatePresenceCode(code, Integer.parseInt(argsPresence[1]), argsPresence[0])) ? ResponseEntity.ok(String.valueOf(code)) : ResponseEntity.badRequest().body(ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString());
        }
    }

    @GetMapping("/events/presences/{eventDesignation}")
    public ResponseEntity<EventResult> getPresencesInEvent(@PathVariable("eventDesignation") String eventDesignation){
        //String[] argsEvent = eventDesignation.split(",");
        eventDesignation = eventDesignation.replace('+', ' ');
        EventResult eventResult = EventManager.getPresencesEvent(eventDesignation);
        //EventResult eventResult = EventManager.queryEventsFilterUser(argsEvent[0], argsEvent[1], argsEvent[2]);

        return eventResult != null ? ResponseEntity.ok(eventResult) : ResponseEntity.badRequest().body(null);
    }

}
