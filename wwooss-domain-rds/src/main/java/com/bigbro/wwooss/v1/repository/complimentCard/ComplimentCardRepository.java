package com.bigbro.wwooss.v1.repository.complimentCard;

import com.bigbro.wwooss.v1.entity.complimentCard.ComplimentCard;
import com.bigbro.wwooss.v1.repository.complimentCard.custom.ComplimentCardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplimentCardRepository extends JpaRepository<ComplimentCard, Long>, ComplimentCardRepositoryCustom {
}
