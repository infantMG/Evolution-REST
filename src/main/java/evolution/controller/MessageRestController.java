package evolution.controller;


import evolution.data.MessageDataService;
import evolution.model.message.Message;
import evolution.security.model.CustomSecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by Infant on 02.09.2017.
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/message")
public class MessageRestController {

    private final MessageDataService messageDataService;

    @Autowired
    public MessageRestController(MessageDataService messageDataService) {
        this.messageDataService = messageDataService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findOne(@PathVariable Long id) {
        Optional optional = messageDataService.findOneRepairDialog(id);
        System.out.println(optional);
        if (!optional.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(optional.get());
    }

    @GetMapping
    public ResponseEntity findAllMessage() {
        List<Message> messages = messageDataService.findAll();
        if (messages.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(messages);
    }

    @GetMapping(value = "/last_from_my_dialog")
    public ResponseEntity findLastMessageForMyDialog() {
        List<Message> list = messageDataService.findLastMessageForDialogAfterRepairDialog();
        if (list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/user/{interlocutorUserId}")
    public ResponseEntity findMessageByUserId(@PathVariable Long interlocutorUserId) {
        List<Message> list = messageDataService.findMessageByUsersRepairDialog(interlocutorUserId, new PageRequest(0, 7));
        if (list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity postMessage(@RequestBody Message message) {
        try {
            Message result = messageDataService.save(message);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteMessageByAuth(@PathVariable Long id) {
        try {
            messageDataService.deleteByAuthentication(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/delete/{id}/admin")
    public ResponseEntity deleteMessageByIdGET(@PathVariable Long id) {
        try {
            messageDataService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}/admin")
    public ResponseEntity deleteMessageById(@PathVariable Long id) {
        try {
            messageDataService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }
}
