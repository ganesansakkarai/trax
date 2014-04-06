package org.kits.trax.repository;

import java.util.List;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.TestType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverageRepository  extends PagingAndSortingRepository<Application, Long> {

	Application findByTimeStampAndTestType(@Param("timeStamp") Long timeStamp, @Param("testType") TestType testType);
	
	@Query("select distinct a.timeStamp from Application a where a.name = :name and a.testType = :testType")
	List<Long> findAllTimeStamp(@Param("name") String name, @Param("testType") TestType testType);
	
	@Query("select distinct a.name from Application a where a.testType = :testType")
	List<String> findApplicationNames(@Param("testType") TestType testType);
}