package isec.pd.meta2.Controller;

import isec.pd.meta2.Shared.ErrorMessages;
import isec.pd.meta2.Shared.EventResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    /*@PostMapping("/login")
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
    public ResponseEntity<String> registerUserInEvent(){


        return ResponseEntity.ok(ErrorMessages.FAIL_REGISTER_PRESENCE_CODE.toString());
    }

    @GetMapping("/presences?timeBegin=&timeEnd=&eventName=")
    public ResponseEntity<EventResult> getPresencesByUsername(){

        return ResponseEntity.ok(null);
    }


}
