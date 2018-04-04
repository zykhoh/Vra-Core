package com.fyp.Repository;

import com.fyp.Model.Solr.ImageIndex;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface IImageIndexRepository extends SolrCrudRepository<ImageIndex, String> {

    @Query("annotation:*?0*")
    public List<ImageIndex> findByAnnotation(String annotation, Pageable pageable);

    @Query("*:*")
    @Facet(fields = "annotation")
    FacetPage<ImageIndex> findAllAndFacetByAnnotation(Pageable pageable);

}
