package com.fyp.Repository;

import com.fyp.Model.Solr.VideoIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Boost;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public interface IVideoIndexRepository extends SolrCrudRepository<VideoIndex, String> {

    Page<VideoIndex> findByTitleContainingOrDescriptionContaining(List<String> title, List<String> description, Pageable pageable);

    @Query(value = "date\\:\\[\\?0\\]")
    Page<VideoIndex> findByDateIs(@DateTimeFormat(pattern = "yyyy-MM-dd") List<LocalDate> dates, Pageable pageable);

}
