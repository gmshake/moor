package tk.blizz.moor.loader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
				c = this.system.loadClass(name);
			} catch (ClassNotFoundException e) {
				ex = e;
			}
		}

		if (c == null && this.parent != this.system && this.parentFirst) {
			try {
				c = this.parent.loadClass(name);
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

		if (c == null && this.parent != this.system && !this.parentFirst) {
			try {
				c = this.parent.loadClass(name);
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
	public URL getResource(String name) {
		URL url = getSystemResource(name);

		if (url == null && this.parent != this.system && this.parentFirst) {
			url = this.parent.getResource(name);
		}

		if (url == null) {
			url = findResource(name);
		}

		if (url == null && this.parent != this.system && !this.parentFirst) {
			url = this.parent.getResource(name);
		}
		return url;
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		final ArrayList<Enumeration<URL>> list = new ArrayList<Enumeration<URL>>(
				3);

		list.add(getSystemResources(name));

		if (this.parent != this.system && this.parentFirst) {
			list.add(this.parent.getResources(name));
		}

		list.add(findResources(name));

		if (this.parent != this.system && !this.parentFirst) {
			list.add(this.parent.getResources(name));
		}

		return new Enumeration<URL>() {
			private final Iterator<Enumeration<URL>> it = list.iterator();
			private Enumeration<URL> em = null;
			private URL url = null;

			private boolean next() {
				if (this.url != null) {
					return true;
				}

				try {
					do {
						if (this.em == null) {
							this.em = this.it.next();
						}

						try {
							this.url = this.em.nextElement();
						} catch (NoSuchElementException e1) {
							this.em = null;
						}
					} while (this.url == null);
				} catch (NoSuchElementException e2) {
				}
				return this.url != null;
			}

			@Override
			public boolean hasMoreElements() {
				return next();
			}

			@Override
			public URL nextElement() {
				if (!next()) {
					throw new NoSuchElementException();
				}
				URL u = this.url;
				this.url = null;
				return u;
			}

		};
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
