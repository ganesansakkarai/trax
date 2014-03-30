package org.kits.trax.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kits.trax.domain.Coverage;
import org.kits.trax.repository.CoverageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoverageServiceImpl implements CoverageService {

	@Autowired
	private CoverageRepository coverageRepository;

	public long countAllCoverages() {

		return coverageRepository.count();
	}

	public void deleteCoverage(Coverage coverage) {

		coverageRepository.delete(coverage);
	}

	public void deleteCoverages(List<Coverage> coverages) {

		for (Coverage coverage : coverages) {
			coverageRepository.delete(coverage);
		}
	}

	public Coverage findCoverage(Long id) {

		return coverageRepository.findOne(id);
	}

	public List<Coverage> findAllCoverages() {

		return (List<Coverage>) coverageRepository.findAll();
	}

	public List<Coverage> findCoverages(Date timeStamp) {

		return coverageRepository.findByTimeStamp(timeStamp);
	}

	public List<Coverage> findCoverages(int page, int maxResults) {

		return coverageRepository.findAll(new PageRequest(page, maxResults)).getContent();
	}

	public Coverage saveCoverage(Coverage coverage) {

		return coverageRepository.save(coverage);
	}

	public List<Coverage> saveCoverages(List<Coverage> coverages) {

		List<Coverage> saved = new ArrayList<Coverage>();
		for (Coverage coverage : coverages) {
			saved.add(coverageRepository.save(coverage));
		}

		return saved;
	}

	public Coverage updateCoverage(Coverage coverage) {

		return coverageRepository.save(coverage);
	}
}
