package org.kits.trax.service;

import java.util.Date;
import java.util.List;

import org.kits.trax.domain.Coverage;

public interface CoverageService {

	public long countAllCoverages();

	public void deleteCoverage(Coverage coverage);

	public Coverage findCoverage(Long id);

	public List<Coverage> findAllCoverages();

	public List<Coverage> findCoverages(Date timeStamp);

	public List<Coverage> findCoverages(int page, int maxResults);

	public Coverage saveCoverage(Coverage coverage);

	public Coverage updateCoverage(Coverage coverage);

	public List<Coverage> saveCoverages(List<Coverage> coverages);

	public void deleteCoverages(List<Coverage> coverages);
}
