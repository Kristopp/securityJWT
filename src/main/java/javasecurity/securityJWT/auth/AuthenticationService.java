package javasecurity.securityJWT.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javasecurity.securityJWT.config.JwtService;
import javasecurity.securityJWT.user.Role;
import javasecurity.securityJWT.user.User;
import javasecurity.securityJWT.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//This is the class where i implement registration and authentication methods
@Service
@RequiredArgsConstructor
public class AuthenticationService {

        // Inject the user repository and password incoder
        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;

        private final JwtService jwtService;

        private final AuthenticationManager authenticationManager;

        // This register method will save the user to the database and return a token
        public AuthenticationResponse register(RegistrationRequest registrationRequest) {
                // We want to create a user object from the registration request
                var user = User.builder()
                                .email(registrationRequest.getEmail())
                                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                                .role(Role.USER)
                                .build();
                userRepository.save(user); // Add @NonNull annotation to fix the problem
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
                var authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                                authenticationRequest.getPassword());
                System.out.println("authenticationRequest.getEmail(): " + authenticationRequest.getEmail());
                authenticationManager.authenticate(authentication);
                var user = userRepository.findByEmail(authenticationRequest.getEmail())
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }
}
