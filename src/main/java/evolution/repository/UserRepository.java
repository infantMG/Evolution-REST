package evolution.repository;

import evolution.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Infant on 07.11.2017.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id =:id")
    Optional<User> findOneUserById(@Param("id") Long userId);

    @Async
    @Query("select u from User u where u.id =:id")
    CompletableFuture<User> findOneUserByIdAsync(@Param("id") Long userId);

    @Query("select u " +
            " from User u " +
            " join fetch u.userAdditionalData ua " +
            " where ua.username =:username ")
    Optional<User> findUserByUsername(@Param("username") String username);

    @Async
    @Query("select u " +
            " from User u " +
            " join fetch u.userAdditionalData ua " +
            " where ua.username =:username ")
    CompletableFuture<User> findUserByUsernameAsync(@Param("username") String username);

    @Query("select u " +
            " from User u " +
            " where u.userAdditionalData =:key ")
    Optional<User> findUserBySecretKey(@Param("key") String secretKey);

    @Query(" select true " +
            "from User u " +
            "where u.userAdditionalData.username =:username ")
    Boolean exist(@Param("username") String username);

    @Query("select u " +
            " from User u " +
            " where u.id =:id " +
            " and u.userAdditionalData.isActive =:active")
    Optional<User> findOneUserByIdAndIsActive(@Param("id") Long userId, @Param("active") boolean active);

    @Query("select u " +
            " from User u " +
            " join fetch u.userAdditionalData as ua " +
            " where u.id =:id " +
            " and ua.isBlock =:isBlock")
    Optional<User> findOneUserByIdAndIsBlockLazy(@Param("id") Long userId, @Param("isBlock") boolean block);

    @Query("select u " +
            " from User u " +
            " where u.id =:id " +
            " and u.userAdditionalData.isBlock =:isBlock")
    Optional<User> findOneUserByIdAndIsBlock(@Param("id") Long userId, @Param("isBlock") boolean block);


    @Query("select u " +
            " from User u " +
            " where u.userAdditionalData.isActive =:active")
    Page<User> findUserAllByIsActive(@Param("active") boolean active, Pageable pageable);

    @Query(value = "select u " +
            " from User u " +
            " join fetch u.userAdditionalData as ua " +
            " where u.userAdditionalData.isActive =:active", countQuery = "select count(u.id) from User u ")
    Page<User> findUserAllByIsActiveLazy(@Param("active") boolean active, Pageable pageable);

    @Query("select u " +
            " from User u " +
            " where u.userAdditionalData.isBlock =:block")
    Page<User> findUserAllByIsBlock(@Param("block") boolean block, Pageable pageable);

    @Query(value = "select u " +
            " from User u " +
            " join fetch u.userAdditionalData as ua " +
            " where u.userAdditionalData.isBlock =:block ", countQuery = "select count(u.id) from User u ")
    Page<User> findUserAllByIsBlockLazy(@Param("block") boolean block, Pageable pageable);

    @Query("select u " +
            " from User u " +
            " where u.userAdditionalData.isActive =:active")
    List<User> findUserAllByIsActive(@Param("active") boolean active, Sort sort);

    @Query("select u " +
            " from User u " +
            " join fetch u.userAdditionalData as ua " +
            " where u.userAdditionalData.isActive =:active")
    List<User> findUserAllByIsActiveLazy(@Param("active") boolean active, Sort sort);

    @Query("select u " +
            " from User u " +
            " where u.userAdditionalData.isBlock =:block")
    List<User> findUserAllByIsBlock(@Param("block") boolean block, Sort sort);

    @Query("select u " +
            " from User u " +
            " join fetch u.userAdditionalData as ua " +
            " where u.userAdditionalData.isBlock =:block")
    List<User> findUserAllByIsBlockLazy(@Param("block") boolean block, Sort sort);

    @Query("select u " +
            " from User u " +
            " where u.userAdditionalData.isActive =:active")
    List<User> findUserAllByIsActive(@Param("active") boolean active);

    @Query("select u " +
            " from User u " +
            " where u.userAdditionalData.isBlock =:block")
    List<User> findUserAllByIsBlock(@Param("block") boolean block);

    @Query("select u " +
            " from User u " +
            " join fetch u.userAdditionalData as ua " +
            " where u.userAdditionalData.isActive =:active")
    List<User> findUserAllByIsActiveLazy(@Param("active") boolean active);

    @Query("select u " +
            " from User u " +
            " join fetch u.userAdditionalData as ua " +
            " where u.userAdditionalData.isBlock =:block")
    List<User> findUserAllByIsBlockLazy(@Param("block") boolean block);

    @Query(value = "select u " +
            "from User u " +
            "join fetch u.userAdditionalData ", countQuery = "select count(u.id) from User u ")
    Page<User> findAllFetchLazy(Pageable pageable);

    @Query("select u from User u " +
            "join fetch u.userAdditionalData ")
    List<User> findAllFetchLazy(Sort sort);

    @Query("select u from User u " +
            "join fetch u.userAdditionalData ")
    List<User> findAllFetchLazy();

    @Query("select c.pk.user from ChannelUserReference c where c.pk.channel.id =:id")
    List<User> findUserByChannel(@Param("id") Long id);

    @Query("select c.pk.user from ChannelUserReference c where c.pk.channel.id =:id")
    List<User> findUserByChannel(@Param("id") Long id, Sort sort);

    @Query("select c.pk.user from ChannelUserReference c where c.pk.channel.id =:id")
    Page<User> findUserByChannel(@Param("id") Long id, Pageable pageable);
}
