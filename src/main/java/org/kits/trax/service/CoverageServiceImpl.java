package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.TestType;
import org.kits.trax.repository.CoverageRepository;
import org.kits.trax.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoverageServiceImpl implements CoverageService {

	@Autowired
	private CoverageRepository coverageRepository;

	public Long countAllApplications() {
	    return coverageRepository.count();
    }

	public Application findApplication(Long id) {
	    
	    return coverageRepository.findOne(id);
    }

	public List<Application> findAllApplications() {
	    
		Iterable<Application> applications = coverageRepository.findAll();
	    return (List<Application>) applications;
    }

	public List<Long> findAllTimeStamps(String name, TestType testType) {
	    
		return coverageRepository.findAllTimeStamp(name, testType);
    }

	public List<String> findApplicationNames(TestType testType) {
	    
		return coverageRepository.findApplicationNames(testType);
    }

	public Application findApplication(Long timeStamp, TestType testType) {
		System.out.println(DateUtil.toString(timeStamp));
		return coverageRepository.findByTimeStampAndTestType(timeStamp, testType);
    }

	public List<Application> findApplications(int page, int maxResults) {
	    Iterable<Application> applications = coverageRepository.findAll(new PageRequest(page, maxResults));
	    return (List<Application>) applications;
    }

	public Application updateApplication(Application application) {
	    
		return coverageRepository.save(application);
    }

	public Application saveApplication(Application application) {
	    
	    return coverageRepository.save(application);
    }

	public void deleteApplication(Long timeStamp, TestType testType) {
	    
		Application application = coverageRepository.findByTimeStampAndTestType(timeStamp, testType);
		coverageRepository.delete(application);
    }
}
