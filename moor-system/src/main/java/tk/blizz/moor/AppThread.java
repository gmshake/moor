package tk.blizz.moor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import tk.blizz.moor.loader.AppClassLoader;

public class AppThread extends Thread {
	private String appClassName;
	private String appContextPath;
	private boolean parentFirst = true;
	private String[] appArgs = new String[] {};

	public void setAppContextPath(String path) {
		this.appContextPath = path;
	}

	public void setParentLoaderPriority(boolean parentFirst) {
		this.parentFirst = parentFirst;
	}

	public void setAppClassName(String name) {
		this.appClassName = name;
	}

	public void setAppArgs(String[] args) {
		this.appArgs = args;
	}

	@Override
	public void run() {
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		AppClassLoader loader = new AppClassLoader(this.appContextPath, parent);
		loader.setParentLoaderPriority(this.parentFirst);

		Method mainMethod;
		try {
			mainMethod = loader.loadClass(this.appClassName).getDeclaredMethod(
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
					+ this.appClassName
					+ " must be declared public and static.");
		}
		// Build the application args array
		try {
			// invoke
			mainMethod.invoke(null, new Object[] { this.appArgs });
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
