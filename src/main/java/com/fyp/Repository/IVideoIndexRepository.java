package com.fyp.Repository;

import com.fyp.Model.Solr.VideoIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Boost;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface IVideoIndexRepository extends SolrCrudRepository<VideoIndex, String> {

    Page<VideoIndex> findByTitleOrDescription(@Boost(2) String title, String description, Pageable pageable);

    

}
