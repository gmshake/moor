package tk.blizz.moor.test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

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

		loader = new CommonClassLoader(new URL[] {}, null, true);
		super.testAll();

		loader = new CommonClassLoader(new URL[] {}, loader, true);

		super.testAll();

		try {
			testChildLoaderParentFirst();
			testChildLoaderParentLast();
		} catch (MalformedURLException e) {
			fail("MalformedURLException");
		}

		try {
			testResourcesChildLoaderParentFirst();
			testResourcesChildLoaderParentLast();
		} catch (IOException e) {
			fail("error create test tmp file");
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

	@Test
	public void testResourcesChildLoaderParentFirst() throws IOException {
		URL url1 = new URL("file:/tmp/");
		URL url2 = new URL("file:/tmp/tmp");

		new File(url1.getFile()).mkdirs();
		new File(url2.getFile()).mkdirs();

		File f1 = new File(url1.getFile(), "test.txt");
		File f2 = new File(url2.getFile(), "test.txt");

		if (!f1.exists())
			assertTrue(f1.createNewFile());

		if (!f2.exists())
			assertTrue(f2.createNewFile());

		CommonClassLoader parent = new CommonClassLoader(new URL[] { url1 });

		CommonClassLoader child1 = new CommonClassLoader(
				new URL[] { url1, url2 }, parent);

		CommonClassLoader child2 = new CommonClassLoader(
				new URL[] { url2, url1 }, parent);

		CommonClassLoader child3 = new CommonClassLoader(
				new URL[] { url2, url1 }, child1);

		testGetResource(parent, child1, "test.txt");
		testGetResource(parent, child2, "test.txt");
		testGetResource(child1, child2, "test.txt");

		// --------------------------------

		testGetResources(child1, child3, "test.txt");
		testGetResources(child1, child2, "test.txt");

	}

	@Test
	public void testResourcesChildLoaderParentLast() throws IOException {
		URL url1 = new URL("file:/tmp/");
		URL url2 = new URL("file:/tmp/tmp");

		new File(url1.getFile()).mkdirs();
		new File(url2.getFile()).mkdirs();

		File f1 = new File(url1.getFile(), "test.txt");
		File f2 = new File(url2.getFile(), "test.txt");

		if (!f1.exists())
			assertTrue(f1.createNewFile());

		if (!f2.exists())
			assertTrue(f2.createNewFile());

		CommonClassLoader parent = new CommonClassLoader(
				new URL[] { url1, url2 });

		CommonClassLoader child1 = new CommonClassLoader(
				new URL[] { url1, url2 }, parent);

		CommonClassLoader child2 = new CommonClassLoader(
				new URL[] { url2, url1 }, parent);

		CommonClassLoader child3 = new CommonClassLoader(
				new URL[] { url2, url1 }, child1);

		URLClassLoader uu = new URLClassLoader(new URL[] { url1, url2 });
		Enumeration<URL> enumuu = uu.getResources("test.txt");
		while (enumuu.hasMoreElements())
			System.out.println(enumuu.nextElement());

		testGetResource(parent, child1, "test.txt");
		testGetResource(parent, child2, "test.txt");
		testGetResource(child1, child2, "test.txt");

		// --------------------------------

		testGetResources(parent, child1, "test.txt");
		testGetResources(parent, child2, "test.txt");

		testGetResources(child1, child2, "test.txt");

	}
}
