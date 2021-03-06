package evolution.repository;

import evolution.model.Dialog;
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
public interface DialogRepository extends JpaRepository<Dialog, Long> {

    @Query("select d " +
            "from Dialog d " +
            "where d.id =:dialogId")
    Optional<Dialog> findOneDialog(@Param("dialogId") Long dialogId);

    @Query("select d from Dialog d join fetch d.messageList where d.id =:id")
    Optional<Dialog> findOneLazy(@Param("id") Long id);

    @Query(" select d " +
            " from Dialog d " +
            " join fetch d.messageList " +
            " where d.id =:id " +
            " and ( d.first.id =:participantId or d.second.id =:participantId )")
    Optional<Dialog> findOneLazyAndParticipantId(@Param("id") Long id, @Param("participantId") Long participantId);

    @Query("select d " +
            "from Dialog d " +
            "where d.id =:dialogId " +
            "and (d.first.id =:iam or d.second.id =:iam)")
    Optional<Dialog> findOneDialog(@Param("dialogId") Long dialogId, @Param("iam") Long iam);

    @Query("select d " +
            "from Dialog d " +
            " where ( d.first.id =:u1 and d.second.id =:u2 ) " +
            " or ( d.first.id =:u2 and d.second.id =:u1 ) ")
    Optional<Dialog> findDialogByUsers(@Param("u1") Long user1, @Param("u2") Long user2);

    @Async
    @Query("select d " +
            "from Dialog d " +
            " where ( d.first.id =:u1 and d.second.id =:u2 ) " +
            " or ( d.first.id =:u2 and d.second.id =:u1 ) ")
    CompletableFuture<Dialog> findDialogByUsersAsync(@Param("u1") Long user1, @Param("u2") Long user2);

    @Query(" select d " +
            " from Dialog d " +
            " where d.first.id =:iam or d.second.id =:iam  ")
    Page<Dialog> findMyDialog(@Param("iam") Long iam, Pageable pageable);

    @Query(" select d " +
            " from Dialog d " +
            " where d.first.id =:iam or d.second.id =:iam  ")
    List<Dialog> findMyDialog(@Param("iam") Long iam, Sort sort);

    @Query(" select d " +
            " from Dialog d " +
            " where d.first.id =:iam or d.second.id =:iam  ")
    List<Dialog> findMyDialog(@Param("iam") Long id);

    @Async
    @Query(" select d " +
            " from Dialog d " +
            " where d.first.id =:iam or d.second.id =:iam  ")
    CompletableFuture<List<Dialog>> findMyDialogAsync(@Param("iam") Long id);

    @Query("select d " +
            "from Dialog d " +
            "join fetch d.messageList ")
    List<Dialog> findAllLazy();

    @Query("select d " +
            "from Dialog d " +
            "join fetch d.messageList ")
    List<Dialog> findAllLazy(Sort sort);

    @Query(value = "select d " +
            "from Dialog d " +
            "join fetch d.messageList ", countQuery = "select count(d.id) from Dialog d")
    Page<Dialog> findAllLazy(Pageable pageable);
}
