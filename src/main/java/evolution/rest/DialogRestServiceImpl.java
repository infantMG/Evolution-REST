package evolution.rest;

import evolution.business.BusinessServiceExecuteResult;
import evolution.business.api.DialogBusinessService;
import evolution.common.BusinessServiceExecuteStatus;
import evolution.dto.model.DialogDTO;
import evolution.dto.model.DialogFullDTO;
import evolution.dto.model.MessageDTO;
import evolution.rest.api.DialogRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by Infant on 05.11.2017.
 */
@Service
public class DialogRestServiceImpl implements DialogRestService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DialogBusinessService dialogBusinessService;

    @Autowired
    public DialogRestServiceImpl(DialogBusinessService dialogBusinessService) {
        this.dialogBusinessService = dialogBusinessService;
    }

    @Override
    public ResponseEntity<List<DialogFullDTO>> findAll() {
        List<DialogFullDTO> list = dialogBusinessService.findAll();
        return response(list);
    }

    @Override
    public ResponseEntity<Page<DialogFullDTO>> findAll(Integer page, Integer size, String sort, List<String> sortProperties) {
        Page<DialogFullDTO> p = dialogBusinessService.findAll(page, size, sort, sortProperties);
        return response(p);
    }

    @Override
    public ResponseEntity<DialogFullDTO> findOne(Long dialogId) {
        Optional<DialogFullDTO> o = dialogBusinessService.findOne(dialogId);
        return response(o);
    }

    @Override
    public ResponseEntity<Page<MessageDTO>> findMessageByDialog(Long dialogId, Integer page, Integer size, String sort, List<String> sortProperties) {
        Page<MessageDTO> p = dialogBusinessService.findMessageByDialog(dialogId, page, size, sort, sortProperties);
        return response(p);
    }

    @Override
    public ResponseEntity<Page<MessageDTO>> findMessageByDialogAndUserId(Long dialogId, Integer page, Integer size, String sort, List<String> sortProperties) {
        Page<MessageDTO> p = dialogBusinessService.findMessageByDialogAndUserId(dialogId, page, size, sort, sortProperties);
        return response(p);
    }

    @Override
    public ResponseEntity<List<MessageDTO>> findMessageByDialogAndUserId(Long dialogId) {
        List<MessageDTO> list = dialogBusinessService.findMessageByDialogAndUserId(dialogId);
        return response(list);
    }

    @Override
    public ResponseEntity<HttpStatus> delete(Long id) {
        BusinessServiceExecuteResult b = dialogBusinessService.delete(id);
        if (b.getExecuteStatus() == BusinessServiceExecuteStatus.OK) {
            return ResponseEntity.noContent().build();
        } else if (b.getExecuteStatus() == BusinessServiceExecuteStatus.EXPECTATION_FAILED){
            return ResponseEntity.status(417).build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }
}
