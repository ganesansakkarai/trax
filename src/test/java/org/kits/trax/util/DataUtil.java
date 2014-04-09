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
		
		Package aPackage = new Package();
		aPackage.setName("org.sample");
		List<Package> packages = new ArrayList<Package>();
		packages.add(aPackage);
		app.setPackages(packages);
				
		Clazz clazz = new Clazz();
		clazz.setName("Hello");
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
