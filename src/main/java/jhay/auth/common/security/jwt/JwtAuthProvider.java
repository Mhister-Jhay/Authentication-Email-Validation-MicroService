package jhay.auth.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jhay.auth.domain.model.User;
import jhay.auth.domain.service.user.UserDetailServiceImpl;
import jhay.auth.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider {
    private final UserDetailServiceImpl userService;
    private final JwtTokenRepository jwtTokenRepository;
    private static final String jwtSecret = "6D597133743677397A24432646294A404E635266546A576E5A7234753778214125442A472D4B6150645367566B58703273357638792F423F4528482B4D625165";
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(DateUtils.getExpirationDate())
                .signWith(getSignatureKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    public String generateRefreshToken(Authentication authentication){
        String username = authentication.getName();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(DateUtils.getExpirationDate())
                .signWith(getSignatureKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    public String getRefreshToken(String accessToken){
        JwtToken jwtToken = jwtTokenRepository.findByAccessToken(accessToken);
        return jwtToken.getRefreshToken();
    }
    @Transactional
    public JwtToken generateNewTokens(String refreshToken){
        String username = extractUsername(refreshToken);
        User user = userService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),user.getPassword());
        JwtToken jwtToken = jwtTokenRepository.findByRefreshToken(refreshToken);
        jwtToken.setAccessToken(generateToken(authentication));
        jwtToken.setRefreshToken(generateRefreshToken(authentication));
        jwtToken.setExpiresAt(DateUtils.getExpirationDate());
        jwtToken.setRefreshedAt(new Date());
        return jwtToken;
    }
    private Key getSignatureKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private Claims extractAllClaims(String jwtToken){
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }
    private <T> T extractSingleClaim(String jwtToken, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }
    private Date extractExpiration(String jwtToken){
        return extractSingleClaim(jwtToken, Claims::getExpiration);
    }
    public boolean isTokenExpired(String jwtToken){
        return extractExpiration(jwtToken).before(new Date());
    }
    public String extractUsername(String jwtToken){
        return extractSingleClaim(jwtToken, Claims::getSubject);
    }
    public boolean isTokenValid(String jwtToken, UserDetails userDetails){
        final String username = extractUsername(jwtToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

}
