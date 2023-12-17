package isec.pd.meta2.Controller;

import isec.pd.meta2.Server.EventManager;
import isec.pd.meta2.Shared.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class AdminController {

    @GetMapping("/events")
    public ResponseEntity<EventResult> getAllEvents(@AuthenticationPrincipal Jwt principal,
                                                    @RequestParam(value ="timeBegin", required = false, defaultValue = " ") String timeBegin,
                                                    @RequestParam(value ="timeEnd", required = false, defaultValue = " ")String timeEnd,
                                                    @RequestParam(value ="eventDesignation", required = false, defaultValue = " ")String eventDesignation,
                                                    @RequestParam(value ="place", required = false, defaultValue = " ")String place){
        String role = principal.getClaimAsString("scope");
        if(role.equalsIgnoreCase("ADMIN")){
            EventResult eventResult;
            ArrayList<Pair<String, String>> parametros = new ArrayList<>();
            parametros.add(new Pair<>("horaInicio", timeBegin));
            parametros.add(new Pair<>("horaFim", timeEnd));
            parametros.add(new Pair<>("designacao", eventDesignation));
            parametros.add(new Pair<>("place", place));

            Optional<Pair<String, String>> option = parametros.stream()
                    .filter(pair -> !" ".equals(pair.second))
                    .findFirst();
            if (option.isPresent()) {
                Pair<String, String> pair = option.get();
                pair.second = pair.second.replace('+', ' ');
                //nome da opcao, opcao
                eventResult = EventManager.queryEventsFilters(pair.first, pair.second);
            } else {
                eventResult = EventManager.queryEvents(null, null);
            }
            return eventResult != null ? ResponseEntity.ok(eventResult) : ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @PostMapping("/events/newEvent")
    public ResponseEntity<String> createEvent(@AuthenticationPrincipal Jwt principal,
                                              @RequestParam(value = "args", required = true, defaultValue = "") String args){
        String role = principal.getClaimAsString("scope");
        if(role.equalsIgnoreCase("ADMIN")){
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
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied!");
    }

    @DeleteMapping("/events/delete/{eventDesignation}")
    public ResponseEntity<String> deleteEvent(@AuthenticationPrincipal Jwt principal,
                                              @PathVariable("eventDesignation") String eventDesignation){
        eventDesignation = eventDesignation.replace('+', ' ');
        String role = principal.getClaimAsString("scope");
        if(role.equalsIgnoreCase("ADMIN"))
            return (EventManager.deleteEvent(eventDesignation)) ? ResponseEntity.ok(Messages.OK.toString()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessages.INVALID_EVENT_NAME.toString());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied!");
    }

    @PostMapping("/events/genCode")
    public ResponseEntity<String> generatePresenceCode(@AuthenticationPrincipal Jwt principal,
                                                       @RequestParam(value = "args", required = true, defaultValue = "") String args){
        String role = principal.getClaimAsString("scope");
        if(role.equalsIgnoreCase("ADMIN")){
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

            if(!EventManager.checkIfCodeAlreadyCreated(argsPresence[0])){
                String code = EventManager.registerPresenceCode(event1, Integer.parseInt(argsPresence[1]), timeAtual);
                return (!code.equals(ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString())) ? ResponseEntity.ok(code) : ResponseEntity.badRequest().body(ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString());
            }
            else{
                int code = EventManager.generateCode();
                return (!EventManager.updatePresenceCode(code, Integer.parseInt(argsPresence[1]), argsPresence[0])) ? ResponseEntity.ok(String.valueOf(code)) : ResponseEntity.badRequest().body(ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied!");
    }

    @GetMapping("/events/presences/{eventDesignation}")
    public ResponseEntity<EventResult> getPresencesInEvent(@AuthenticationPrincipal Jwt principal,
                                                           @PathVariable("eventDesignation") String eventDesignation){
        String role = principal.getClaimAsString("scope");
        if(role.equalsIgnoreCase("ADMIN")) {
            eventDesignation = eventDesignation.replace('+', ' ');
            EventResult eventResult = EventManager.getPresencesEvent(eventDesignation);
            return eventResult != null ? ResponseEntity.ok(eventResult) : ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

}
