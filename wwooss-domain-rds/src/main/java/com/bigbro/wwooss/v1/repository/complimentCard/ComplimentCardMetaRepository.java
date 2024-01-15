package com.bigbro.wwooss.v1.repository.complimentCard;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCardMeta;
import com.bigbro.wwooss.v1.enumType.CardCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplimentCardMetaRepository extends JpaRepository<ComplimentCardMeta, Long> {
    Optional<ComplimentCardMeta> findByCardCode(CardCode cardCode);
}
