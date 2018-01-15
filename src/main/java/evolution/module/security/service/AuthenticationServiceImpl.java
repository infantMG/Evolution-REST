package evolution.module.security.service;

import evolution.module.security.dto.AuthenticationResponse;
import evolution.module.security.dto.CustomSecurityUser;
import evolution.module.security.token.JwtTokenService;
import evolution.module.user.dto.transfer.UserDTOTransfer;
import evolution.user.common.UserRoleEnum;
import evolution.module.security.crud.api.AuthenticationSessionCrudManagerService;
import evolution.security.model.AuthenticationSession;
import evolution.security.dto.AuthenticationRequest;
import evolution.security.dto.JwtCleanToken;
import evolution.service.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    private final UserDetailsService userDetailsService;

    private final AuthenticationSessionCrudManagerService authenticationSessionCrudManagerService;

    private final DateService dateService;

    private final SecuritySupportService securitySupportService;

    private final UserDTOTransfer userDTOTransfer;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     JwtTokenService jwtTokenService,
                                     UserDetailsService userDetailsService,
                                     AuthenticationSessionCrudManagerService authenticationSessionCrudManagerService,
                                     DateService dateService,
                                     SecuritySupportService securitySupportService,
                                     UserDTOTransfer userDTOTransfer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
        this.authenticationSessionCrudManagerService = authenticationSessionCrudManagerService;
        this.dateService = dateService;
        this.securitySupportService = securitySupportService;
        this.userDTOTransfer = userDTOTransfer;
    }

    @Override
    @Transactional
    public ResponseEntity<AuthenticationResponse> authenticationRequest(AuthenticationRequest authenticationRequest) throws AuthenticationException {

        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        CustomSecurityUser userDetails = (CustomSecurityUser) this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String token;

        Optional<AuthenticationSession> op = authenticationSessionCrudManagerService.findByUsername(userDetails.getUsername());
        if (!op.isPresent()) {
            String session = UUID.randomUUID().toString();
            token = jwtTokenService.generateToken(userDetails, session);
            AuthenticationSession jwt = new AuthenticationSession(userDetails.getUsername(), session, dateService.getCurrentDateInUTC());
            authenticationSessionCrudManagerService.save(jwt);
        } else {
            token = this.jwtTokenService.generateToken(userDetails, op.get().getAuthSession());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("TOKEN " + token);
        return ResponseEntity.ok(new AuthenticationResponse(token, userDTOTransfer.modelToDTO(userDetails)));
    }

    @Override
    @Transactional
    public ResponseEntity<AuthenticationResponse> authenticationRequestAdmin(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        CustomSecurityUser userDetails = (CustomSecurityUser) this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        if (userDetails.getUser().getRole() != UserRoleEnum.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        String token;

        Optional<AuthenticationSession> op = authenticationSessionCrudManagerService.findByUsername(userDetails.getUsername());
        if (!op.isPresent()) {
            String session = UUID.randomUUID().toString();
            token = jwtTokenService.generateToken(userDetails, session);
            AuthenticationSession jwt = new AuthenticationSession(userDetails.getUsername(), session, dateService.getCurrentDateInUTC());
            authenticationSessionCrudManagerService.save(jwt);
        } else {
            token = this.jwtTokenService.generateToken(userDetails, op.get().getAuthSession());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("TOKEN " + token);
        return ResponseEntity.ok(new AuthenticationResponse(token, userDTOTransfer.modelToDTO(userDetails)));
    }

    @Override
    public ResponseEntity<HttpStatus> cleanAuthenticationSessionKey(JwtCleanToken jwtCleanToken) {
        Optional<CustomSecurityUser> optional = securitySupportService.getPrincipal();
        if (optional.isPresent()) {
            if (optional.get().getUser().getRole() == UserRoleEnum.ADMIN || optional.get().getUser().getUserAdditionalData().getUsername().equals(jwtCleanToken.getUsername())) {
                authenticationSessionCrudManagerService.delete(jwtCleanToken.getUsername());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(403).build();
            }
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<AuthenticationResponse> refreshAuthenticationSessionKey(JwtCleanToken jwtCleanToken) {
        Optional<CustomSecurityUser> optional = securitySupportService.getPrincipal();
        if (optional.isPresent()) {
            if (optional.get().getUser().getRole() == UserRoleEnum.ADMIN || optional.get().getUser().getUserAdditionalData().getUsername().equals(jwtCleanToken.getUsername())) {
                authenticationSessionCrudManagerService.delete(jwtCleanToken.getUsername());
                String session = UUID.randomUUID().toString();
                AuthenticationSession jwt = new AuthenticationSession(jwtCleanToken.getUsername(), session, dateService.getCurrentDateInUTC());
                authenticationSessionCrudManagerService.save(jwt);
                String token = this.jwtTokenService.generateToken(jwtCleanToken.getUsername(), session);
                return ResponseEntity.ok(new AuthenticationResponse(token, userDTOTransfer.modelToDTO(optional.get().getUser())));
            } else {
                return ResponseEntity.status(403).build();
            }
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}
