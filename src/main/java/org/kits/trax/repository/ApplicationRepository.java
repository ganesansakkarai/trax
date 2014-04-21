package org.kits.trax.repository;

import org.kits.trax.domain.Application;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends PagingAndSortingRepository<Application, Long> {

	@Query("select a from Application a where a.name = :name")
	public Application findByName(@Param("name") String name);
}
