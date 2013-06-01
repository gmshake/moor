package tk.blizz.moor.test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import tk.blizz.moor.loader.CommonClassLoader;

public class CommonClassLoaderTest extends ClassLoaderTest {

	@Override
	@Test
	public void testAll() {
		/**
		 * all basic test
		 */
		loader = new CommonClassLoader(new URL[] {});
		super.testAll();

		loader = new CommonClassLoader(new URL[] {}, null, false);
		super.testAll();

		loader = new CommonClassLoader(new URL[] {}, loader, false);
		super.testAll();

		try {
			testChildLoaderParentFirst();
			testChildLoaderParentLast();
		} catch (MalformedURLException e) {
			fail("MalformedURLException");
		}

	}

	/**
	 * provide custome class/resource before test, make sure
	 * org.mortbay.jetty.webapp.WebAppClassLoader is NOT in classpath
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testChildLoaderParentFirst() throws MalformedURLException {
		URL url = new URL("jar:file:/tmp/jetty-6.1.26.jar!/");

		CommonClassLoader parent = new CommonClassLoader(new URL[] { url });

		CommonClassLoader child = new CommonClassLoader(new URL[] { url },
				parent);

		CommonClassLoader child2 = new CommonClassLoader(new URL[] {}, parent);

		Class<?> WebAppClassLoaderClass = null;
		try {
			WebAppClassLoaderClass = system
					.loadClass("org.mortbay.jetty.webapp.WebAppClassLoader");
		} catch (ClassNotFoundException e1) {
		}
		assertSame(WebAppClassLoaderClass, null);

		assertSame(
				testLoadSameClass(parent, child,
						"org.mortbay.jetty.webapp.WebAppClassLoader"), parent);
		assertSame(
				testLoadSameClass(parent, child2,
						"org.mortbay.jetty.webapp.WebAppClassLoader"), parent);
		assertSame(
				testLoadSameClass(child, child2,
						"org.mortbay.jetty.webapp.WebAppClassLoader"), parent);

	}

	/**
	 * make sure org.mortbay.jetty.webapp.WebAppClassLoader is NOT in classpath
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testChildLoaderParentLast() throws MalformedURLException {
		URL url = new URL("jar:file:/tmp/jetty-6.1.26.jar!/");

		// top classloader
		CommonClassLoader parent = new CommonClassLoader(new URL[] { url });

		CommonClassLoader parent2 = new CommonClassLoader(new URL[] {}, parent,
				false);

		CommonClassLoader child1 = new CommonClassLoader(new URL[] {}, parent2,
				false);

		CommonClassLoader child2 = new CommonClassLoader(new URL[] { url },
				parent2, false);

		Class<?> WebAppClassLoaderClass = null;
		try {
			WebAppClassLoaderClass = system
					.loadClass("org.mortbay.jetty.webapp.WebAppClassLoader");
		} catch (ClassNotFoundException e1) {
		}
		assertSame(WebAppClassLoaderClass, null);

		assertSame(
				testLoadSameClass(parent, parent2,
						"org.mortbay.jetty.webapp.WebAppClassLoader"), parent);

		assertSame(
				testLoadSameClass(parent2, child1,
						"org.mortbay.jetty.webapp.WebAppClassLoader"), parent);

		assertSame(
				testLoadDifferentClass(child2, parent2,
						"org.mortbay.jetty.webapp.WebAppClassLoader"), child2);

	}
}
