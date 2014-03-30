package org.kits.trax.repository;

import java.util.Date;
import java.util.List;

import org.kits.trax.domain.Coverage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverageRepository  extends PagingAndSortingRepository<Coverage, Long> {

	List<Coverage> findByTimeStamp(@Param("timeStamp") Date timeStamp);
}