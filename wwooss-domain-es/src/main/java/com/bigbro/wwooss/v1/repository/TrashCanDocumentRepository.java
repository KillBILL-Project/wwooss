package com.bigbro.wwooss.v1.repository;

import com.bigbro.wwooss.v1.document.TrashCanDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TrashCanDocumentRepository extends ElasticsearchRepository<TrashCanDocument, Long> {

}
