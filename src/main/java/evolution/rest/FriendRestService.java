package evolution.rest;

import evolution.model.Friend;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by Infant on 14.10.2017.
 */
public interface FriendRestService {

    ResponseEntity<List<Friend>> findUserFollower(Long userId, Integer page, Integer size);

    ResponseEntity<List<Friend>> findUserRequest(Long userId, Integer page, Integer size);

    ResponseEntity<List<Friend>> findUserProgress(Long userId, Integer page, Integer size);

    ResponseEntity<Friend> sendRequest(Long userId);

    ResponseEntity<Friend> removeRequest(Long userId);

    ResponseEntity<Friend> removeFriend(Long userId);

    ResponseEntity<Friend> acceptRequest(Long userId);

    ResponseEntity<List<Friend>> findAll(Integer page, Integer size);
}
