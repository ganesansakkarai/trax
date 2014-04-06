package org.kits.trax.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.Package;
import org.kits.trax.domain.TestType;

public class DataUtil {

	public static Application build() {

		Application app = new Application();
		app.setName("Sample");
		app.setTimeStamp(new Date().getTime());
		app.setTestType(TestType.Unit);
		app.setLine(100);
		app.setMissedLine(90);
		app.setBranch(50);
		app.setMissedBranch(40);
		
		Package aPackage = new Package();
		aPackage.setName("org.sample");
		app.setLine(100);
		app.setMissedLine(90);
		app.setBranch(50);
		app.setMissedBranch(40);
		List<Package> packages = new ArrayList<Package>();
		packages.add(aPackage);
		app.setPackages(packages);
				
		Clazz clazz = new Clazz();
		clazz.setName("Hello");
		clazz.setLine(100);
		clazz.setMissedLine(90);
		clazz.setBranch(50);
		clazz.setMissedBranch(40);
		List<Clazz> classes = new ArrayList<Clazz>();
		classes.add(clazz);
		aPackage.setClazzes(classes);
		
		List<Method> methods = new ArrayList<Method>();
		for (int i = 0; i < 10; ++i) {
			Method method = new Method();
			method.setName("method" + i);
			method.setLine(10);
			method.setMissedLine(9);
			method.setBranch(5);
			method.setMissedBranch(4);
			methods.add(method);
		}
		clazz.setMethods(methods);
		
		

		return app;
	}
}
