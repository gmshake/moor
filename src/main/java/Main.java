import tk.blizz.moor.loader.SharedLibClassLoader;

public class Main implements Runnable {
	@Override
	public void run() {
		SharedLibClassLoader monitor = new SharedLibClassLoader(
				"/tmp/moor/monitor/", ClassLoader.getSystemClassLoader());

		SharedLibClassLoader shared = new SharedLibClassLoader(
				"/tmp/moor/shared/", ClassLoader.getSystemClassLoader());

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
			appstarter = (Runnable) shared
					.loadClass("tk.blizz.moor.AppStarter").newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		Thread t = new Thread(appstarter, "appstarter");
		t.setContextClassLoader(shared);
		t.start();
	}

}
