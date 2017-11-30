package evolution.rest;



import evolution.business.BusinessServiceExecuteResult;
import evolution.business.api.MessageBusinessService;
import evolution.common.BusinessServiceExecuteStatus;
import evolution.dto.model.MessageDTO;
import evolution.dto.model.MessageDTOForSave;
import evolution.dto.model.MessageForUpdateDTO;
import evolution.rest.api.MessageRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Created by Infant on 28.10.2017.
 */
@Service
public class MessageRestServiceImpl implements MessageRestService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MessageBusinessService messageBusinessService;

    @Autowired
    public MessageRestServiceImpl(MessageBusinessService messageBusinessService) {
        this.messageBusinessService = messageBusinessService;
    }

    @Override
    public ResponseEntity<List<MessageDTO>> findAll() {
        List<MessageDTO> list = messageBusinessService.findAll();
        return response(list);
    }

    @Override
    public ResponseEntity<Page<MessageDTO>> findAllMessage(Integer page, Integer size, String sort, List<String> sortProperties) {
        Page<MessageDTO> p = messageBusinessService.findAll(page, size, sort, sortProperties);
        return response(p);
    }

    @Override
    public ResponseEntity<Page<MessageDTO>> findMessageRecipientId(Long recipient, Integer page, Integer size, String sort, List<String> sortProperties) {
        Page<MessageDTO> p = messageBusinessService.findMessageByRecipientId(recipient, page, size, sort, sortProperties);
        return response(p);
    }

    @Override
    public ResponseEntity<Page<MessageDTO>> findMessageSenderId(Long sender, Integer page, Integer size, String sort, List<String> sortProperties) {
        Page<MessageDTO> p = messageBusinessService.findMessageBySenderId(sender, page, size, sort, sortProperties);
        return response(p);
    }

    @Override
    public ResponseEntity<Page<MessageDTO>> findLastMessageInMyDialog(Long userId, Integer page, Integer size, String sort, List<String> sortProperties) {
        Page<MessageDTO> p = messageBusinessService.findLastMessageInMyDialog(userId, page, size, sort, sortProperties);
        return response(p);
    }

    @Override
    public ResponseEntity<List<MessageDTO>> findLastMessageInMyDialog(Long userId) {
        List<MessageDTO> list = messageBusinessService.findLastMessageInMyDialog(userId);
        return response(list);
    }

    @Override
    public ResponseEntity<MessageDTO> findOneMessage(Long id) {
        Optional<MessageDTO> m = messageBusinessService.findOne(id);
        return response(m);
    }

    @Override
    public ResponseEntity<MessageDTO> save(MessageDTOForSave message) {
        BusinessServiceExecuteResult<MessageDTO> b = messageBusinessService.createMessage(message.getSenderId(), message.getRecipientId(), message.getText());
        if (b.getExecuteStatus() == BusinessServiceExecuteStatus.OK && b.getResultObjectOptional().isPresent()) {
            return ResponseEntity.status(201).body(b.getResultObjectOptional().get());
        } else if (b.getExecuteStatus() == BusinessServiceExecuteStatus.FORBIDDEN) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.status(417).build();
    }

    @Override
    public ResponseEntity<HttpStatus> update(MessageForUpdateDTO message) {
        return null;
    }

    @Override
    public ResponseEntity<MessageForUpdateDTO> updateAfterReturn(MessageForUpdateDTO message) {
        return null;
    }

    @Override
    public ResponseEntity<HttpStatus> updateMessage(MessageForUpdateDTO message) {
        return null;
    }

    @Override
    public ResponseEntity<HttpStatus> delete(Long messageId) {
        BusinessServiceExecuteResult b = messageBusinessService.delete(messageId);
        if (b.getExecuteStatus() == BusinessServiceExecuteStatus.OK) {
            return ResponseEntity.noContent().build();
        } else if (b.getExecuteStatus() == BusinessServiceExecuteStatus.NOT_FOUNT_OBJECT_FOR_EXECUTE) {
            return ResponseEntity.status(417).build();
        }
        return ResponseEntity.status(500).build();
    }

    @Override
    public ResponseEntity<Long> deleteAfterReturnId(Long messageId) {
        return null;
    }

    @Override
    public ResponseEntity<Page<MessageDTO>> findMessageByDialog(Long dialogId, Integer page, Integer size, String sort, List<String> sortProperties) {
        Page<MessageDTO> p = messageBusinessService.findMessageByDialogId(dialogId, page, size, sort, sortProperties);
        return response(p);
    }

    @Override
    public ResponseEntity<List<MessageDTO>> findMessageByDialog(Long dialogId, String sort, List<String> sortProperties) {
        return null;
    }

    @Override
    public ResponseEntity<List<MessageDTO>> findMessageByDialog(Long dialogId) {
        List<MessageDTO> list = messageBusinessService.findMessageByDialogId(dialogId);
        return response(list);
    }

    @Override
    public ResponseEntity<List<MessageDTO>> findMessageByInterlocutor(Long interlocutor) {
        List<MessageDTO> list = messageBusinessService.findMessageByInterlocutor(interlocutor);
        return response(list);
    }

    @Override
    public ResponseEntity<Page<MessageDTO>> findMessageByInterlocutor(Long interlocutor, Integer page, Integer size, String sort, List<String> sortProperties) {
        Page<MessageDTO> p = messageBusinessService.findMessageByInterlocutor(interlocutor, page, size, sort, sortProperties);
        return response(p);
    }

}
