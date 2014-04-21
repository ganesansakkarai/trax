package org.kits.trax.repository;

import java.util.List;

import org.kits.trax.domain.Build;
import org.kits.trax.domain.TestType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildRepository extends PagingAndSortingRepository<Build, Long> {

	@Query("select b from Build b where b.application.id = :id and b.testType = :testType")
	public List<Build> list(@Param("id") Long id, @Param("testType") TestType testType);

	@Query("select b from Build b left join fetch b.modules where b.id = :id")
	public Build find(@Param("id") Long id);
}