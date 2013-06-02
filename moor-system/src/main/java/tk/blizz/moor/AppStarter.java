package tk.blizz.moor;

import org.apache.log4j.Logger;

import tk.blizz.moor.loader.SharedLibClassLoader;

public class AppStarter implements Runnable {
	private static final Logger log = Logger.getLogger(AppStarter.class);

	@Override
	public void run() {
		log.info("start...");

		SharedLibClassLoader shared = new SharedLibClassLoader(
				"/tmp/moor/lib/", ClassLoader.getSystemClassLoader());

		try {
			Thread t1 = (Thread) shared.loadClass(
					"tk.blizz.moor.AppContainer").newInstance();

			Thread t2 = (Thread) shared.loadClass(
					"tk.blizz.moor.AppContainer").newInstance();

			Thread t3 = (Thread) shared.loadClass(
					"tk.blizz.moor.AppContainer2").newInstance();

			t1.setContextClassLoader(shared);
			t2.setContextClassLoader(shared);
			t3.setContextClassLoader(shared);

			t1.setName("app1");
			t2.setName("app2");
			t3.setName("app3");
			t1.start();
			t2.start();
			t3.start();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		log.info("exit.");
	}

}
