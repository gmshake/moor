package tk.blizz.moor;

import org.apache.log4j.Logger;

import tk.blizz.moor.loader.AppClassLoader;
import tk.blizz.moor.loader.SharedLibClassLoader;

public class AppStarter implements Runnable {
	private static final Logger log = Logger.getLogger(AppStarter.class);

	@Override
	public void run() {
		log.info("start...");

		SharedLibClassLoader sharedLib = new SharedLibClassLoader(
				"/tmp/moor/lib/", ClassLoader.getSystemClassLoader());

		AppClassLoader app1 = new AppClassLoader("/tmp/moor/apps/app1/",
				sharedLib);
		AppClassLoader app2 = new AppClassLoader("/tmp/moor/apps/app2/",
				sharedLib);
		AppClassLoader app3 = new AppClassLoader("/tmp/moor/apps/app3/",
				sharedLib);

		app2.setParentLoaderPriority(false);
		app3.setParentLoaderPriority(false);

		try {
			Thread t1 = (Thread) app1.loadClass("tk.blizz.moor.AppContainer")
					.newInstance();

			Thread t2 = (Thread) app2.loadClass("tk.blizz.moor.AppContainer")
					.newInstance();

			Thread t3 = (Thread) app3.loadClass("tk.blizz.moor.AppContainer2")
					.newInstance();

			t1.setContextClassLoader(app1);
			t2.setContextClassLoader(app2);
			t3.setContextClassLoader(app3);

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
