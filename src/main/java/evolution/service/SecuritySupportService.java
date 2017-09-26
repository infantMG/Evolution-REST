package evolution.service;

import evolution.common.UserRoleEnum;
import evolution.model.user.User;
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
            LOGGER.info("fail instanceof for object = " + object);
        }
        return Optional.empty();
    }

    public boolean isAllowed(Long id) {
        Optional<CustomSecurityUser> principal = getPrincipal();

        if (!principal.isPresent()) {
            LOGGER.warn("principal is null");
            return false;
        }

        User user = principal.get().getUser();
        LOGGER.info("principal is = " + user);

        return user.getRoleId().equals(UserRoleEnum.ADMIN.getId()) || user.getId().equals(id);
    }

}