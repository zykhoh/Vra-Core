package com.fyp.Repository;

import com.fyp.Model.Solr.ImageIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface IImageIndexRepository extends SolrCrudRepository<ImageIndex, String> {

    @Query("annotation:?0")
    Page<ImageIndex> findByAnnotation(String annotation, Pageable pageable);

    @Query("*:*")
    @Facet(fields = {"annotation"})
    FacetPage<ImageIndex> findAllFacetByAnnotation(Pageable pageable);

    @Query("videoId:?0 AND annotation:[* TO *]")
    @Facet(fields = {"annotation"})
    FacetPage<ImageIndex> findByVideoIdFacetOnAnnotation(Long videoId, Pageable pageable);

}
