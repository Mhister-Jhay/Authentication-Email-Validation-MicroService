package jhay.auth.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jhay.auth.application.model.RegistrationRequest;
import jhay.auth.application.model.RegistrationResponse;
import jhay.auth.common.security.token.TokenValidationServiceImpl;
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
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email,
                                                 HttpServletRequest request) {
        return new ResponseEntity<>(tokenService.forgotPassword(email, request), HttpStatus.OK);
    }
    @GetMapping("/verify-reset-password")
    public ResponseEntity<String> verifyPasswordToken(@RequestParam("token") String token,
                                                      HttpServletRequest request) {
        return new ResponseEntity<>(tokenService.validatePasswordToken(token, request), HttpStatus.OK);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email,
                                                @RequestParam("password") String password){
        return new ResponseEntity<>(userService.resetPassword(email,password),HttpStatus.OK);
    }
}
