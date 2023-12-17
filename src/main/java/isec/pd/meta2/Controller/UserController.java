package isec.pd.meta2.Controller;

import isec.pd.meta2.Server.EventManager;
import isec.pd.meta2.Server.UserManager;
import isec.pd.meta2.Shared.*;
import isec.pd.meta2.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class UserController {

    /*
    @PostMapping("/login")
    public ResponseEntity<String> login(){

        return ResponseEntity.ok(ErrorMessages.INVALID_PASSWORD.toString());
    }*/

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam(value = "name", required=true, defaultValue = "") String name,
                                           @RequestParam(value = "id", required=true, defaultValue = "") String id,
                                           @RequestParam(value = "username", required=true, defaultValue = "") String username,
                                           @RequestParam(value = "password", required=true, defaultValue = "") String password){
        Register register = new Register(name, id, username, password);
        return UserManager.registerUser(register) ? ResponseEntity.ok(Messages.OK.toString()) : ResponseEntity.badRequest().body(ErrorMessages.INVALID_USER.toString());
    }

    @PostMapping("/codEvent")
    public ResponseEntity<String> registerUserInEvent(@AuthenticationPrincipal Jwt principal,
                                                      @RequestBody String args){
        String[] argsEvent = args.split(",");
        String role = principal.getClaimAsString("scope");
        if(role.equalsIgnoreCase("USER")){
            return (EventManager.registerUserInEvent(argsEvent[0], Integer.parseInt(argsEvent[1]))) ? ResponseEntity.ok(Messages.REGISTER_PRESENCE_CODE.toString()) : ResponseEntity.badRequest().body(ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied!");
    }

    @GetMapping("/presences/{username}")
    public ResponseEntity<EventResult> getPresencesByUsername(@AuthenticationPrincipal Jwt principal,
                                                              @PathVariable String username,
                                                              @RequestParam(value ="timeBegin", required = false, defaultValue = " ") String timeBegin,
                                                              @RequestParam(value ="timeEnd", required = false, defaultValue = " ")String timeEnd,
                                                              @RequestParam(value ="eventDesignation", required = false, defaultValue = " ")String eventDesignation,
                                                              @RequestParam(value ="place", required = false, defaultValue = " ")String place){
        String role = principal.getClaimAsString("scope");
        if(role.equalsIgnoreCase("USER")) {
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
                eventResult = EventManager.queryEventsFilterUser(pair.first, pair.second, username);
            } else {
                ArrayList<Integer> idsUser = EventManager.getIdsEventsByUsername(username);
                String filter = null;
                EventResult eventResultEvents = new EventResult(" ");
                eventResultEvents.setColumns(" ");
                if(idsUser.size() == 0){
                    return ResponseEntity.badRequest().body(null);
                }
                filter = EventManager.createFilterOr(idsUser);
                eventResult = EventManager.queryEvents(username, filter);
            }
            return eventResult != null ? ResponseEntity.ok(eventResult) : ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<String> isAdmin(@AuthenticationPrincipal Jwt principal){
        String role = principal.getClaimAsString("scope");
        if(role.equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.ok(ErrorMessages.LOGIN_ADMIN_USER.toString());
        }
        return ResponseEntity.ok(ErrorMessages.LOGIN_NORMAL_USER.toString());
    }
}
