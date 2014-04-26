package org.kits.trax.repository;

import java.util.List;

import org.kits.trax.domain.Build;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildRepository extends PagingAndSortingRepository<Build, Long> {
	
	@Query("select distinct b.name from Build b where b.parent is null")
	public List<String> listApplications();

	@Query("select b from Build b where b.name = :name and b.parent is null")
	public List<Build> listBuilds(@Param("name") String name);
	
	@Query("select b from Build b where b.parent.id = :id")
	public List<Build> listModules(@Param("id") Long id);
}