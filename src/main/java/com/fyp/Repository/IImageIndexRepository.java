package com.fyp.Repository;

import com.fyp.Model.Solr.ImageIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface IImageIndexRepository extends SolrCrudRepository<ImageIndex, String> {

}
