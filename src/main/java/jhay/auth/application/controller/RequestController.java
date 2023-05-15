package jhay.auth.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jhay.auth.application.model.AuthResponse;
import jhay.auth.domain.service.request.RequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestServiceImpl requestService;
    @PostMapping("/validate")
    public ResponseEntity<AuthResponse> validateRequest(@RequestHeader("Authorization") String authHeader,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response){
        return new ResponseEntity<>(requestService.validateRequest(authHeader,request,response), HttpStatus.OK);
    }
}
