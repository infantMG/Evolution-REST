package evolution.module.friend.crud;

import evolution.friend.common.RelationshipStatus;
import evolution.module.friend.crud.api.FriendCrudManagerService;
import evolution.module.friend.model.Friend;
import evolution.module.friend.repository.FriendRepository;
import evolution.module.user.model.User;
import evolution.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Infant on 08.11.2017.
 */
@Service
public class FriendCrudManagerServiceImpl implements FriendCrudManagerService {

    @Value("${model.second.maxfetch}")
    private Integer friendMaxFetch;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final FriendRepository friendRepository;

    private final UserRepository userRepository;

    private Long firstId;

    private Long secondId;

    @Autowired
    public FriendCrudManagerServiceImpl(FriendRepository friendRepository,
                                        UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Friend> findRandomFriends(Long user, Integer size) {
        return friendRepository.findRandomFriend(user, RelationshipStatus.ACCEPTED, new PageRequest(0, size));
    }

    @Override
    public Page<Friend> findAll(Integer page, Integer size) {
        Pageable pageable = getPageableForRestService(page, size,
                this.friendMaxFetch);
        return friendRepository.findAll(pageable);
    }

    @Override
    public List<Friend> findAll() {
        return friendRepository.findAll();
    }

    @Override
    public Optional<Friend> findOneFriend(Long user1, Long user2) {
        init(user1, user2);
        return friendRepository.findOneFriend(firstId, secondId);
    }

    @Override
    public Page<Friend> findFollowerByUser(Long userId, Integer page, Integer size) {
        Pageable pageable = getPageableForRestService(page, size,
                this.friendMaxFetch);
        return friendRepository.findFollowerByUser(userId, RelationshipStatus.PENDING, pageable);
    }

    @Override
    public Page<Friend> findRequestByUser(Long userId, Integer page, Integer size) {
        Pageable pageable = getPageableForRestService(page, size,
                this.friendMaxFetch);
        return friendRepository.findRequestFromUser(userId, RelationshipStatus.PENDING, pageable);
    }

    @Override
    public Page<Friend> findProgressByUser(Long userId, Integer page, Integer size) {
        Pageable pageable = getPageableForRestService(page, size,
                this.friendMaxFetch);
        return friendRepository.findProgressByUser(userId, RelationshipStatus.ACCEPTED, pageable);
    }

    @Override
    public List<Friend> findFollowerByUser(Long userId) {
        return friendRepository.findFollowerByUser(userId, RelationshipStatus.PENDING);
    }

    @Override
    public List<Friend> findRequestByUser(Long userId) {
        return friendRepository.findRequestFromUser(userId, RelationshipStatus.PENDING);
    }

    @Override
    public List<Friend> findProgressByUser(Long userId) {
        return friendRepository.findProgressByUser(userId, RelationshipStatus.ACCEPTED);
    }

    @Override
    public Page<Friend> findRequestFromUser(Long userId, Integer page, Integer size) {
        Pageable pageable = getPageableForRestService(page, size,
                this.friendMaxFetch);
        return friendRepository.findRequestFromUser(userId, RelationshipStatus.PENDING, pageable);
    }

    @Override
    public List<Friend> findRequestFromUser(Long userId) {
        return friendRepository.findRequestFromUser(userId, RelationshipStatus.PENDING);
    }

    @Override
    @Transactional
    public Optional<Friend> sendRequestToFriend(Long senderId, Long recipientId) {
        User action = null;

        init(senderId, recipientId);
        Optional<Friend> exist = friendRepository.findOneFriend(firstId, secondId);
        Optional<User> of = userRepository.findOneUserById(firstId);
        Optional<User> os = userRepository.findOneUserById(secondId);

        if (!of.isPresent() || !os.isPresent()) {
            return exist;
        }

        if (!exist.isPresent()) {

            User first = of.get();
            User second = os.get();

            if (first.getId().equals(senderId)) {
                action = first;
            }

            if (second.getId().equals(senderId)) {
                action = second;
            }

            Friend friend = new Friend(first, second, RelationshipStatus.PENDING, action);
            return Optional.of(friendRepository.save(friend));
        } else {
            LOGGER.info("sendRequest second failed. Action =  " + senderId + ", other " + recipientId + ". Find row = " + exist.get());
            return exist;
        }
    }

    @Override
    @Transactional
    public Optional<Friend> removeRequest(Long senderId, Long recipientId) {
        init(senderId, recipientId);

        Optional<Friend> request = findOneFriend(firstId, secondId);

        if (request.isPresent()) {

            boolean m = RelationshipStatus.PENDING == request.get().getStatus()
                    && request.get().getActionUser().equalsById(senderId);

            if (m) {
                friendRepository.delete(request.get());
                LOGGER.info("remove sendRequest successful");
                return Optional.of(new Friend(RelationshipStatus.NOT_FOUND));
            } else {
                LOGGER.info("remove sendRequest failed. Action =  " + senderId + ", other " + recipientId + ". Find row = " + request.get());
                return request;
            }

        }

        return Optional.of(new Friend(RelationshipStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public Optional<Friend> removeFriend(Long senderId, Long recipientId) {
        init(senderId, recipientId);
        User action;
        Optional<Friend> progress = findOneFriend(firstId, secondId);

        if (progress.isPresent()) {

            if (RelationshipStatus.ACCEPTED == progress.get().getStatus()) {
                Friend friend = progress.get();
                friend.setStatus(RelationshipStatus.PENDING);

                action = getUserByIdFromFriendPk(recipientId, friend); // Типа мне кидают заявку

                friend.setActionUser(action);

                return Optional.of(friendRepository.save(friend));
            } else {
                LOGGER.info("remove second failed. Action =  " + senderId + ", other " + recipientId + ". Find row = " + progress.get());
                return progress;
            }

        }

        return Optional.of(new Friend(RelationshipStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public Optional<Friend> acceptRequest(Long senderId, Long recipientId) {
        User action;

        init(senderId, recipientId);

        Optional<Friend> request = findOneFriend(firstId, secondId);
        if (request.isPresent()) {

            boolean m = request.get().getStatus() == RelationshipStatus.PENDING
                    && request.get().getActionUser().equalsById(recipientId);

            if (m) {
                Friend friend = request.get();
                friend.setStatus(RelationshipStatus.ACCEPTED);

                action = getUserByIdFromFriendPk(senderId, friend);

                friend.setActionUser(action);
                return Optional.of(friendRepository.save(friend));
            } else {
                LOGGER.info("accept sendRequest failed. Action =  " + senderId + ", other " + recipientId + ". Find row = " + request.get());
                return request;
            }

        }

        return Optional.of(new Friend(RelationshipStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public void clearRowByUserForeignKey(Long id) {
        List<Friend> list = friendRepository.findRowByIam(id);
        friendRepository.delete(list);
    }

    private void init(Long senderOrAction, Long recipient) {
        if (senderOrAction > recipient) {
            firstId = senderOrAction;
            secondId = recipient;
        } else {
            firstId = recipient;
            secondId = senderOrAction;
        }
    }

    private User getUserByIdFromFriendPk(Long userId, Friend friend) {
        if (friend.getPk().getFirst().getId().equals(userId))
            return friend.getPk().getFirst();
        else if (friend.getPk().getSecond().getId().equals(userId))
            return friend.getPk().getSecond();
        else
            throw new UnsupportedOperationException("Friend primary key not have user by id " + userId);
    }
}
