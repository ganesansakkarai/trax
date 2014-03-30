package org.kits.trax.repository;

import java.util.List;

import org.kits.trax.domain.Coverage;
import org.kits.trax.domain.TestResult;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface TestResultRepository  extends PagingAndSortingRepository<TestResult, Long> {

	List<Coverage> findByTimeStamp(@Param("timeStamp") String timeStamp);
}
