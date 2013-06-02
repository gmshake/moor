package tk.blizz.moor.loader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

/**
 * This loader defaults to the 2.3 servlet spec behaviour where non system
 * classes are loaded from the classpath in preference to the parent loader.
 * Java2 compliant loading, where the parent loader always has priority, can be
 * selected with the
 * {@link tk.blizz.moor.loader.CommonClassLoader#setParentLoaderPriority(boolean)}
 * 
 * If no parent class loader is provided, then the current thread context
 * classloader will be used. If that is null then the classloader that loaded
 * this class is used as the parent.
 * 
 * @author zlei.huang@gmail.com
 * 
 */
public class CommonClassLoader extends URLClassLoader {
	/**
	 * default to java2 compliant, ie: delegate to parent classloader first
	 */
	private boolean parentFirst = true;
	private final ClassLoader system;
	private final ClassLoader parent;

	public CommonClassLoader(URL[] urls) {
		this(urls, null);
	}

	public CommonClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent != null ? parent : (Thread.currentThread()
				.getContextClassLoader() != null ? Thread.currentThread()
				.getContextClassLoader() : (CommonClassLoader.class
				.getClassLoader() != null ? CommonClassLoader.class
				.getClassLoader() : ClassLoader.getSystemClassLoader())));

		this.system = getSystemClassLoader();
		this.parent = getParent();
	}

	public CommonClassLoader(URL[] urls, ClassLoader parent, boolean parentFirst) {
		this(urls, parent);
		this.parentFirst = parentFirst;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return loadClass(name, false);
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		// First, check if the class has already been loaded
		Class<?> c = findLoadedClass(name);
		ClassNotFoundException ex = null;
		if (c == null) {
			// always use system class loader first
			try {
				c = system.loadClass(name);
			} catch (ClassNotFoundException e) {
				ex = e;
			}
		}

		if (c == null && parent != system && parentFirst) {
			try {
				c = parent.loadClass(name);
			} catch (ClassNotFoundException e) {
				ex = e;
			}
		}

		if (c == null) {
			try {
				c = findClass(name);
			} catch (ClassNotFoundException e) {
				ex = e;
			}
		}

		if (c == null && parent != system && !parentFirst) {
			try {
				c = parent.loadClass(name);
			} catch (ClassNotFoundException e) {
				ex = e;
			}
		}

		if (c == null)
			throw ex;

		if (resolve) {
			resolveClass(c);
		}
		return c;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}

	@Override
	public URL getResource(String name) {
		URL url = getSystemResource(name);

		if (url == null && parent != system && parentFirst) {
			url = parent.getResource(name);
		}

		if (url == null) {
			url = findResource(name);
		}

		if (url == null && parent != system && !parentFirst) {
			url = parent.getResource(name);
		}
		return url;
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		Enumeration<URL> e = getSystemResources(name);

		if (e == null && parent != system && parentFirst) {
			e = parent.getResources(name);
		}

		if (e == null) {
			e = findResources(name);
		}

		if (e == null && parent != system && !parentFirst) {
			e = parent.getResources(name);
		}
		return e;
	}

	/**
	 * set parent class loader priority
	 * 
	 * @param parentFirst
	 */
	public void setParentLoaderPriority(boolean parentFirst) {
		this.parentFirst = parentFirst;
	}

	/**
	 * get parent class loader priority
	 * 
	 * @return
	 */
	public boolean getParentLoaderPriority() {
		return this.parentFirst;
	}
}
