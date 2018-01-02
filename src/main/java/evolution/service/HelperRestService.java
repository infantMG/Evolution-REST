package evolution.service;

import evolution.business.BusinessServiceExecuteResult;
import evolution.common.BusinessServiceExecuteStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HelperRestService {

    public <T> ResponseEntity<Page<T>> response(Page<T> page) {
        if (page == null || page.getContent() == null || page.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(page);
    }

    public <T> ResponseEntity<List<T>> response(List<T> list) {
        if (list == null || list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    public <T> ResponseEntity<T> response(Optional<T> optional) {
        if (optional == null || !optional.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(optional.get());
    }

    public <T> ResponseEntity<T> response(BusinessServiceExecuteResult<T> executeResult) {
        if (executeResult.getExecuteStatus() == BusinessServiceExecuteStatus.OK && executeResult.getResultObjectOptional() != null && executeResult.getResultObjectOptional().isPresent()) {
            return ResponseEntity.ok(executeResult.getResultObject());
        } else if (executeResult.getExecuteStatus() == BusinessServiceExecuteStatus.OK && executeResult.getResultObjectOptional() == null) {
            return ResponseEntity.ok().build();
        } else if (executeResult.getExecuteStatus() == BusinessServiceExecuteStatus.FORBIDDEN) {
            return ResponseEntity.status(403).build();
        } else if (executeResult.getExecuteStatus() == BusinessServiceExecuteStatus.NOT_FOUNT_OBJECT_FOR_EXECUTE) {
            return ResponseEntity.status(204).build();
        } else if (executeResult.getExecuteStatus() == BusinessServiceExecuteStatus.NOT_FOUND_PRINCIPAL_FOR_EXECUTE) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.status(417).build();
    }

}