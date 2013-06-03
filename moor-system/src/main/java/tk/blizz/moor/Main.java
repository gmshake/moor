package tk.blizz.moor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import tk.blizz.moor.loader.SharedLibClassLoader;

public class Main implements Runnable {
	@Override
	public void run() {
		final Logger log = Logger.getLogger(this.getClass());

		log.info("main start...");
		log.debug("main classloader: " + this.getClass().getClassLoader());

		File moorHome = new File(System.getProperty("moor.home"));

		URL[] monitorlib;
		try {
			monitorlib = new URL[] {
					new File(moorHome, "/lib/system.jar").toURI().toURL(),
					new File(moorHome, "/lib/log4j.jar").toURI().toURL(), };
		} catch (MalformedURLException e1) {
			throw new RuntimeException(e1);
		}

		SharedLibClassLoader monitor = new SharedLibClassLoader(monitorlib,
				ClassLoader.getSystemClassLoader());

		log.debug("setup monitor starter's classloader: " + monitor);

		URL[] appslib;
		try {
			appslib = new URL[] {
					new File(moorHome, "/lib/system.jar").toURI().toURL(),
					new File(moorHome, "/lib/log4j.jar").toURI().toURL(), };
		} catch (MalformedURLException e1) {
			throw new RuntimeException(e1);
		}

		SharedLibClassLoader appstarterLoader = new SharedLibClassLoader(
				appslib, ClassLoader.getSystemClassLoader());

		log.debug("setup app starter's classloader: " + appstarterLoader);

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

		log.debug("start monitor starter ...");
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

		log.debug("start app starter ...");
		t.start();

		log.info("main end.");
	}

}
