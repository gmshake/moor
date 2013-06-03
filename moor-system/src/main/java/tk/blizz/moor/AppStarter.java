package tk.blizz.moor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import tk.blizz.moor.loader.SharedLibClassLoader;

public class AppStarter implements Runnable {
	private static final Logger log = Logger.getLogger(AppStarter.class);

	private Method getMethod(Class<?> appThreadClass, String name,
			Class<?>... parameterTypes) {
		try {
			return appThreadClass.getDeclaredMethod(name, parameterTypes);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void invokeMethod(Object o, Method method, Object[] args) {
		try {
			method.invoke(o, args);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	private void setAppContainerParameters(Thread appThread,
			String appContextPath, boolean parentFirst, String appClassName,
			String[] appArgs) {
		invokeMethod(
				appThread,
				getMethod(appThread.getClass(), "setAppContextPath",
						String.class), new Object[] { appContextPath });
		invokeMethod(
				appThread,
				getMethod(appThread.getClass(), "setParentLoaderPriority",
						boolean.class), new Object[] { parentFirst });
		invokeMethod(
				appThread,
				getMethod(appThread.getClass(), "setAppClassName", String.class),
				new Object[] { appClassName });
		invokeMethod(appThread,
				getMethod(appThread.getClass(), "setAppArgs", String[].class),
				new Object[] { appArgs });
	}

	@Override
	public void run() {
		log.info("start...");

		SharedLibClassLoader shared = new SharedLibClassLoader(
				"/tmp/moor/lib/", ClassLoader.getSystemClassLoader());

		try {
			Thread t1 = (Thread) shared.loadClass("tk.blizz.moor.AppContainer")
					.newInstance();

			Thread t2 = (Thread) shared.loadClass("tk.blizz.moor.AppContainer")
					.newInstance();

			Thread t3 = (Thread) shared.loadClass("tk.blizz.moor.AppContainer")
					.newInstance();

			t1.setContextClassLoader(shared);
			t2.setContextClassLoader(shared);
			t3.setContextClassLoader(shared);

			t1.setName("app1");
			t2.setName("app2");
			t3.setName("app3");

			setAppContainerParameters(t1, "/tmp/moor/apps/app1/", true,
					"tk.blizz.moor.apps.App", new String[] {});
			setAppContainerParameters(t2, "/tmp/moor/apps/app2/", false,
					"tk.blizz.moor.apps.App", new String[] {});
			setAppContainerParameters(t3, "/tmp/moor/apps/app3/", false,
					"tk.blizz.moor.apps.AnotherApp", new String[] {});

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
