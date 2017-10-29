package evolution.service;

import evolution.common.UserRoleEnum;
import evolution.exception.AuthenticationPrincipalNotFoundException;
import evolution.model.User;
import evolution.security.model.CustomSecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Infant on 09.09.2017.
 */
@Service
public class SecuritySupportService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public Optional<CustomSecurityUser> getPrincipal() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(object instanceof String) && object instanceof CustomSecurityUser) {
            CustomSecurityUser customSecurityUser = (CustomSecurityUser) object;
            return Optional.of(customSecurityUser);
        } else {
            LOGGER.info("fail instanceof for object = " + object + ". Maybe authentication not exist");
        }
        return Optional.empty();
    }

    public CustomSecurityUser getAuthenticationPrincipal() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object instanceof CustomSecurityUser) {
            return (CustomSecurityUser) object;
        } else {
            throw new AuthenticationPrincipalNotFoundException("not found authentication in security context");
        }
    }

    public boolean isAllowed(Long id) {
        User user = getAuthenticationPrincipal().getUser();
        return user.getRole().name().equals(UserRoleEnum.ADMIN.name()) || user.getId().equals(id);
    }
}
