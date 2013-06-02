package tk.blizz.moor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import tk.blizz.moor.loader.AppClassLoader;

public class AppContainer extends Thread {

	@Override
	public void run() {
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		AppClassLoader loader = new AppClassLoader("/tmp/moor/apps/"
				+ Thread.currentThread().getName() + "/", parent);
		loader.setParentLoaderPriority(false);

		String appClassName = "tk.blizz.moor.apps.App";
		Method mainMethod;
		try {
			mainMethod = loader.loadClass(appClassName).getDeclaredMethod(
					"main", String[].class);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		// Make sure that the method is public and static
		int modifiers = mainMethod.getModifiers();
		if (!(Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers))) {
			throw new RuntimeException("The main method in class "
					+ appClassName + " must be declared public and static.");
		}
		// Build the application args array
		String[] appArgs = new String[] {};
		try {
			// invoke
			mainMethod.invoke(null, new Object[] { appArgs });
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
