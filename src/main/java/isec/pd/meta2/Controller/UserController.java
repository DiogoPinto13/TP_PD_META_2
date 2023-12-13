package isec.pd.meta2.Controller;

import isec.pd.meta2.Server.EventManager;
import isec.pd.meta2.Shared.ErrorMessages;
import isec.pd.meta2.Shared.EventResult;
import isec.pd.meta2.Shared.Messages;
import isec.pd.meta2.Shared.Pair;
import isec.pd.meta2.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> register(){

        return ResponseEntity.ok(ErrorMessages.INVALID_USERNAME.toString());
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<Boolean> isAdmin() {

        return ResponseEntity.ok(true);
    }

    @PostMapping("/codEvent")
    public ResponseEntity<String> registerUserInEvent(@AuthenticationPrincipal Jwt principal,
                                                      @RequestParam(value = "presenceCode", required = true, defaultValue = "") String presenceCode,
                                                      @RequestParam(value = "username", required=true, defaultValue = "") String username){
        String role = principal.getClaimAsString("scope");
        if(role.equalsIgnoreCase("USER")){
            return (EventManager.registerUserInEvent(username, Integer.parseInt(presenceCode))) ? ResponseEntity.ok(Messages.REGISTER_PRESENCE_CODE.toString()) : ResponseEntity.badRequest().body(ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied!");
    }

    @GetMapping("/presences")
    public ResponseEntity<EventResult> getPresencesByUsername(@AuthenticationPrincipal Jwt principal,
                                                              @RequestParam(value ="timeBegin", required = false, defaultValue = " ") String timeBegin,
                                                              @RequestParam(value ="timeEnd", required = false, defaultValue = " ")String timeEnd,
                                                              @RequestParam(value ="eventDesignation", required = false, defaultValue = " ")String eventDesignation,
                                                              @RequestParam(value ="place", required = false, defaultValue = " ")String place,
                                                              @RequestParam(value ="username", required = true, defaultValue = "")String username){
        String role = principal.getClaimAsString("scope");
        if(role.equalsIgnoreCase("USER")) {
            EventResult eventResult;
            ArrayList<Pair<String, String>> parametros = new ArrayList<>();
            parametros.add(new Pair<>("timeBegin", timeBegin));
            parametros.add(new Pair<>("timeEnd", timeEnd));
            parametros.add(new Pair<>("eventDesignation", eventDesignation));
            parametros.add(new Pair<>("place", place));

            Optional<Pair<String, String>> option = parametros.stream()
                    .filter(pair -> !" ".equals(pair.second))
                    .findFirst();
            if (option.isPresent()) {
                Pair<String, String> pair = option.get();
                eventResult = EventManager.queryEventsFilterUser(pair.first, pair.second, username);
            } else {
                eventResult = EventManager.queryEvents(username, null);
            }
            return eventResult != null ? ResponseEntity.ok(eventResult) : ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
}
