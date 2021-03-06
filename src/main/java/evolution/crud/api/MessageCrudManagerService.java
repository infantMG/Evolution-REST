package evolution.crud.api;

import evolution.dto.model.MessageSaveDTO;
import evolution.dto.model.MessageUpdateDTO;
import evolution.model.Message;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Infant on 07.11.2017.
 */
public interface MessageCrudManagerService extends AbstractCrudManagerService<Message, Long>, PageableManager {

    Optional<Message> findOne(Long messageId, Long senderId);

    List<Message> findMessageByDialogId(Long dialogId);

    List<Message> findMessageByDialogId(Long dialogId, String sort, List<String> sortProperties);

    Page<Message> findMessageByDialogId(Long dialogId, Integer page, Integer size, String sort, List<String> sortProperties);

    Page<Message> findMessageByDialogId(Long dialogId, Long iam, Integer page, Integer size, String sort, List<String> sortProperties);

    List<Message> findMessageByDialogIdAndParticipant(Long dialogId, Long participantId);

    List<Message> findMessageByDialogIdAndParticipant(Long dialogId, Long participantId, String sort, List<String> sortProperties);

    Page<Message> findMessageByDialogIdAndParticipant(Long dialogId, Long participantId, Integer page, Integer size, String sort, List<String> sortProperties);

    List<Message> findMessageByDialogId(Long dialogId, Long iam, String sort, List<String> sortProperties);

    List<Message> findMessageByDialogId(Long dialogId, Long iam);

    Page<Message> findLastMessageInMyDialog(Long iam, Integer page, Integer size, String sort, List<String> sortProperties);

    List<Message> findLastMessageInMyDialog(Long iam);

    List<Message> findLastMessageInMyDialog(Long iam, String sort, List<String> sortProperties);

    Page<Message> findMessageBySenderId(Long senderId, Integer page, Integer size, String sort, List<String> sortProperties);

    List<Message> findMessageBySenderId(Long senderId);

    List<Message> findMessageBySenderId(Long senderId, String sort, List<String> sortProperties);

    Page<Message> findMessageByRecipientId(Long recipientId, Integer page, Integer size, String sort, List<String> sortProperties);

    List<Message> findMessageByRecipientId(Long recipientId, String sort, List<String> sortProperties);

    List<Message> findMessageByRecipientId(Long recipientId);

    Optional<Message> update(Message message);

    void deleteMessageAndMaybeDialog(Long messageId);

    void deleteMessageAndMaybeDialog(Long messageId, Long senderId);

    void deleteMessageAndMaybeDialog(List<Long> ids);

    void deleteMessageAndMaybeDialog(List<Long> ids, Long senderId);

    Message saveMessageAndMaybeCreateNewDialog(String text, Long senderId, Long recipientId, Date createDateUTC);

    Optional<Message> update(MessageUpdateDTO messageUpdateDTO);

    Optional<Message> update(MessageUpdateDTO messageUpdateDTO, Long senderId);

    Message saveMessageAndMaybeCreateNewDialog(MessageSaveDTO messageSaveDTO, Date createUTC);

    List<Message> findMessageByInterlocutor(Long interlocutor, Long second);

    List<Message> findMessageByInterlocutor(Long interlocutor, Long second, String sortType, List<String> sortProperties);

    Page<Message> findMessageByInterlocutor(Long interlocutor, Long second, Integer page, Integer size, String sort, List<String> sortProperties);
}
