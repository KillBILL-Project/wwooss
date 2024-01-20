package com.bigbro.wwooss.v1.repository;

import com.bigbro.wwooss.v1.document.TrashCanDocument;
import com.bigbro.wwooss.v1.repository.custom.TrashCanDocumentRepositoryCustom;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TrashCanDocumentRepository extends ElasticsearchRepository<TrashCanDocument, Long>, TrashCanDocumentRepositoryCustom {
    List<TrashCanDocument> findTrashCanDocumentByTrashType(String trashType);
}
