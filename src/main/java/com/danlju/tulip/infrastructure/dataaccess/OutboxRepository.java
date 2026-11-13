package com.danlju.tulip.infrastructure.dataaccess;
import com.danlju.tulip.infrastructure.dataaccess.entity.OutboxMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.LockModeType;
import java.util.List;
public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from OutboxMessage m where m.publishedAt is null order by m.id")
    List<OutboxMessage> pending(Pageable page);
}
