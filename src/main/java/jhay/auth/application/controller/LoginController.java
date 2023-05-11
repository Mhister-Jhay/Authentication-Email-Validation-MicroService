package jhay.auth.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import jhay.auth.application.model.AuthResponse;
import jhay.auth.application.model.LoginRequest;
import jhay.auth.domain.service.login.LoginServiceImpl;
import jhay.auth.domain.service.token.TokenValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    private final LoginServiceImpl loginService;
    private final TokenValidationServiceImpl tokenValidationService;

    @PostMapping("/")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(loginService.loginUser(loginRequest), HttpStatus.OK);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> userForgotPassword(@RequestParam("email") String email,
                                                     HttpServletRequest request){
        return new ResponseEntity<>(loginService.forgotPassword(email,request), HttpStatus.OK);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email,
                                                @RequestParam("password") String password){
        return new ResponseEntity<>(loginService.resetPassword(email,password),HttpStatus.OK);
    }
    @GetMapping("/verify-reset-password")
    public ResponseEntity<String> verifyPasswordToken(@RequestParam("token") String token,
                                                      HttpServletRequest request) {
        return new ResponseEntity<>(tokenValidationService.validatePasswordToken(token, request), HttpStatus.OK);
    }
}
