package jhay.auth.application.registration.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jhay.auth.application.registration.model.RegistrationRequest;
import jhay.auth.application.registration.model.RegistrationResponse;
import jhay.auth.domain.security.token.TokenValidationServiceImpl;
import jhay.auth.domain.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserServiceImpl userService;
    private final TokenValidationServiceImpl tokenService;

    @PostMapping("/")
    public ResponseEntity<RegistrationResponse> registerNewUser(@Valid @RequestBody
                                                                RegistrationRequest registerRequest,
                                                                HttpServletRequest request){
        return new ResponseEntity<>(userService.registerUser(registerRequest,request), HttpStatus.CREATED);
    }
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUserEmail(@RequestParam("token") String token,
                                                  HttpServletRequest request){
        return new ResponseEntity<>(tokenService.validateToken(token,request),HttpStatus.OK);
    }
    @GetMapping("/request-new-token")
    public ResponseEntity<String> requestNewToken(@RequestParam("email") String email,
                                                  HttpServletRequest request){
        return new ResponseEntity<>(tokenService.requestNewToken(email,request), HttpStatus.OK);
    }
}
