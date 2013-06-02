package tk.blizz.moor;

import tk.blizz.moor.loader.AppClassLoader;

public class AppStarter implements Runnable {

	@Override
	public void run() {
		AppClassLoader app1 = new AppClassLoader("/tmp/moor/apps/app1/");
		AppClassLoader app2 = new AppClassLoader("/tmp/moor/apps/app2/");

		try {
			Thread t1 = (Thread) app1.loadClass("tk.blizz.moor.AppContainer")
					.newInstance();

			Thread t2 = (Thread) app2.loadClass("tk.blizz.moor.AppContainer")
					.newInstance();

			t1.setContextClassLoader(app1);
			t2.setContextClassLoader(app2);

			t1.setName("app1");
			t2.setName("app2");
			t1.start();
			t2.start();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

	}

}
