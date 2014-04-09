package org.kits.trax.service;

import java.util.List;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.TestType;
import org.kits.trax.repository.CoverageRepository;
import org.kits.trax.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CoverageServiceImpl implements CoverageService {

	@Autowired
	private CoverageRepository coverageRepository;

	public Application saveApplication(Application application) {

		for (org.kits.trax.domain.Package aPackage : application.getPackages()) {
			for(Clazz clazz : aPackage.getClazzes()) {
				for(Method method : clazz.getMethods()) {
					clazz.setLine(clazz.getLine() + method.getLine());
					clazz.setMissedLine(clazz.getMissedLine() + method.getMissedLine());
					clazz.setBranch(clazz.getBranch() + method.getBranch());
					clazz.setMissedBranch(clazz.getMissedBranch() + method.getMissedBranch());
					method.setCoverage(((method.getLine() - method.getMissedLine())/method.getLine()) * 100);
				}
				
				aPackage.setLine(aPackage.getLine() + clazz.getLine());
				aPackage.setMissedLine(aPackage.getMissedLine() + clazz.getMissedLine());
				aPackage.setBranch(aPackage.getBranch() + clazz.getBranch());
				aPackage.setMissedBranch(aPackage.getMissedBranch() + clazz.getMissedBranch());
				clazz.setCoverage(((clazz.getLine() - clazz.getMissedLine())/clazz.getLine()) * 100);				
			}
			
			application.setLine(application.getLine() + aPackage.getLine());
			application.setMissedLine(application.getMissedLine() + aPackage.getMissedLine());
			application.setBranch(application.getBranch() + aPackage.getBranch());
			application.setMissedBranch(application.getMissedBranch() + aPackage.getMissedBranch());
			aPackage.setCoverage(((aPackage.getLine() - aPackage.getMissedLine())/aPackage.getLine()) * 100);
		}
		
		application.setCoverage(((application.getLine() - application.getMissedLine())/application.getLine()) * 100);
		
		return coverageRepository.save(application);
	}

	public List<String> listApplications(TestType testType) {

		return coverageRepository.findApplicationNames(testType);
	}

	public List<Long> listBuilds(String name, TestType testType) {

		return coverageRepository.findAllTimeStamp(name, testType);
	}

	public Application findApplication(String name, TestType testType, Long timeStamp) {
		System.out.println(DateUtil.toString(timeStamp));
		return coverageRepository.findByTimeStampAndTestType(timeStamp, testType);
	}

	public void deleteApplication(String name, TestType testType, Long timeStamp) {

		Application application = coverageRepository.findByTimeStampAndTestType(timeStamp, testType);
		coverageRepository.delete(application);
	}
}
