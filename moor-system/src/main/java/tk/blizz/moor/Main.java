package tk.blizz.moor;

import tk.blizz.moor.loader.SharedLibClassLoader;

public class Main implements Runnable {
	@Override
	public void run() {
		SharedLibClassLoader monitor = new SharedLibClassLoader(
				"/tmp/moor/bin/moor.jar:/tmp/moor/bin/log4j.jar",
				ClassLoader.getSystemClassLoader());

		SharedLibClassLoader appstarterLoader = new SharedLibClassLoader(
				"/tmp/moor/bin/moor.jar:/tmp/moor/bin/log4j.jar",
				ClassLoader.getSystemClassLoader());

		Runnable monitorstarter;
		try {
			monitorstarter = (Runnable) monitor.loadClass(
					"tk.blizz.moor.MonitorStarter").newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		Thread m = new Thread(monitorstarter, "monitor");
		m.setContextClassLoader(monitor);
		m.start();

		Runnable appstarter;
		try {
			appstarter = (Runnable) appstarterLoader.loadClass(
					"tk.blizz.moor.AppStarter").newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		Thread t = new Thread(appstarter, "appstarter");
		t.setContextClassLoader(appstarterLoader);
		t.start();
	}

}
