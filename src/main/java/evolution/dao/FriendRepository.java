package evolution.dao;

import evolution.common.FriendStatusEnum;
import evolution.model.friend.Friends;
import evolution.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Infant on 08.08.2017.
 */
public interface FriendRepository extends JpaRepository<Friends, Long> {

    @Query("select f from Friends f where f.user.id =:userId and f.status =:status")
    List<Friends> findFriendsByStatusAndUser(Long userId, Long status);

//    select * from friends f WHERE f.friend_id = 216 and f.user_id = 226 and status != 3;

    @Query("select f from Friends f where f.user.id =:authUserId and f.friend.id =:firneUserId and f.status <> :statusProgress")
    Friends checkFriends(Long authUserId, Long friendUserId, Long statusProgress);

    @Query("select f from Friends f where f.user.id =:authUserId and f.friend.id =:firneUserId and f.status =:status")
    Friends getFriendsByUserIdAndStatus(Long authUserId, Long friendUserId, Long status);


//    //todo: in future repair this
//    @Query(" FROM Friends f, StandardUser u " +
//            " where u.id = f.friend.id and f.user.id = :authUserId" +
//            " and u.id = :id ")
//    Friends findUserAndFriendStatus(@Param("authUserId") Long authUserId, @Param("id") Long id);
//
//
//    @Query("select count(f) from Friends f where f.user.id = :user_id and f.status = :status_id")
//    Long countFriendsByStatus(@Param("user_id") Long userId, @Param("status_id") Long statusId);
//
//    @Query(" select f " +
//            " from  Friends f " +
//            " where f.user.id = :user_id " +
//            " and f.status = :status_id "  +
//            " order by rand() ")
//    List<User> randomFriends(@Param("user_id") Long userId, @Param("status_id") Long statusId, Pageable pageable);
}