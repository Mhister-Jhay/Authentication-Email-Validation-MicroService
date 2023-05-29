package jhay.auth.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jhay.auth.application.model.RegistrationRequest;
import jhay.auth.application.model.AuthResponse;
import jhay.auth.domain.service.registration.RegistrationServiceImpl;
import jhay.auth.domain.service.token.TokenValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationServiceImpl registrationService;
    private final TokenValidationServiceImpl tokenService;

    @PostMapping("/")
    public ResponseEntity<AuthResponse> registerNewUser(@Valid @RequestBody
                                                                RegistrationRequest registerRequest,
                                                        HttpServletRequest request){
        return new ResponseEntity<>(registrationService.registerUser(registerRequest,request), HttpStatus.CREATED);
    }
    @GetMapping("/hello")
    public String get(){
        return "Hello, from azure";
    }
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyUserEmail(@RequestParam("token") String token,
                                                  HttpServletRequest request){
        return new ResponseEntity<>(tokenService.validateToken(token,request),HttpStatus.OK);
    }
    @GetMapping("/request-new-verification-token")
    public ResponseEntity<String> requestNewToken(@RequestParam("email") String email,
                                                  HttpServletRequest request){
        return new ResponseEntity<>(tokenService.requestNewVerificationToken(email,request), HttpStatus.OK);
    }

}
