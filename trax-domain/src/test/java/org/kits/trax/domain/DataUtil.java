package org.kits.trax.domain;

import java.util.Date;

import org.kits.trax.domain.Application;
import org.kits.trax.domain.Clazz;
import org.kits.trax.domain.Method;
import org.kits.trax.domain.Module;
import org.kits.trax.domain.TestType;

public class DataUtil {

	public static Application build() {

		Application app = new Application();
		app.setName("sample");
		app.setTimeStamp(new Date().getTime());
		app.setTestType(TestType.UNIT);
		
		Module module = new Module();
		module.setName("sample.module");
		app.getModules().add(module);

		Clazz clazz = new Clazz();
		clazz.setName("Sample");
		module.getClazzes().add(clazz);

		for (int i = 0; i < 10; ++i) {
			Method method = new Method();
			method.setName("method" + i);
			method.setLine(10);
			method.setMissedLine(9);
			method.setBranch(5);
			method.setMissedBranch(4);
			clazz.getMethods().add(method);
		}

		return app;
	}
}
