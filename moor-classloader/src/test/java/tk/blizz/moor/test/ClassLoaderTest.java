package tk.blizz.moor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * base classloader test
 * 
 * @author zlei.huang@gmail.com
 * 
 */
public abstract class ClassLoaderTest {
	/**
	 * set loader before basic test
	 */
	protected ClassLoader loader;

	protected ClassLoader system = ClassLoader.getSystemClassLoader();

	protected void testAll() {

		assertSame(testLoadSameClass(system, loader, "java.lang.Object"), null);

		assertSame(
				testLoadSameClass(system, loader, this.getClass().getName()),
				system);

		testGetResource(loader, system, "java/lang/Object.class");
		testGetResource(loader, system,
				this.getClass().getName().replace('.', '/').concat(".class"));
		testGetResources(loader, system, "java/lang/Object.class");
		testGetResources(loader, system,
				this.getClass().getName().replace('.', '/').concat(".class"));
	}

	protected ClassLoader testLoadSameClass(ClassLoader loader1,
			ClassLoader loader2, String name) {
		Class<?> ObjectClass1 = null;
		Class<?> ObjectClass2 = null;
		try {
			ObjectClass1 = loader1.loadClass(name);
			ObjectClass2 = loader2.loadClass(name);
		} catch (ClassNotFoundException e) {
			fail("class " + name + " not found");
		}

		assertNotNull(ObjectClass1);
		assertNotNull(ObjectClass2);
		assertSame(ObjectClass1, ObjectClass2);
		return ObjectClass1.getClassLoader();
	}

	protected ClassLoader testLoadDifferentClass(ClassLoader loader1,
			ClassLoader loader2, String name) {
		Class<?> ObjectClass1 = null;
		Class<?> ObjectClass2 = null;
		try {
			ObjectClass1 = loader1.loadClass(name);
			ObjectClass2 = loader2.loadClass(name);
		} catch (ClassNotFoundException e) {
			fail("class " + name + " not found");
		}

		assertNotNull(ObjectClass1);
		assertNotNull(ObjectClass2);
		assertNotSame(ObjectClass1, ObjectClass2);
		return ObjectClass1.getClassLoader();
	}

	protected URL testGetResource(ClassLoader loader1, ClassLoader loader2,
			String name) {
		URL url1 = null;
		URL url2 = null;

		url1 = loader1.getResource(name);
		url2 = loader2.getResource(name);

		assertNotNull(url1);
		assertNotNull(url2);
		assertEquals(url1, url2);
		return url1;
	}

	protected URL testGetResourceDifferent(ClassLoader loader1,
			ClassLoader loader2, String name) {
		URL url1 = null;
		URL url2 = null;

		url1 = loader1.getResource(name);
		url2 = loader2.getResource(name);

		assertNotNull(url1);
		assertNotNull(url2);
		assertFalse(url1.equals(url2));
		return url1;
	}

	protected Set<URL> testGetResources(ClassLoader loader1,
			ClassLoader loader2, String name) {
		Enumeration<URL> e1 = null;
		Enumeration<URL> e2 = null;

		try {
			e1 = loader1.getResources(name);
			e2 = loader2.getResources(name);
		} catch (IOException e) {
			fail("resource " + name + " not found");
		}

		assertNotNull(e1);
		assertNotNull(e2);

		LinkedHashSet<URL> urls1 = new LinkedHashSet<URL>(100);
		LinkedHashSet<URL> urls2 = new LinkedHashSet<URL>(100);

		while (e1.hasMoreElements()) {
			urls1.add(e1.nextElement());
		}

		while (e2.hasMoreElements()) {
			urls2.add(e2.nextElement());
		}

		assertTrue(urls1.size() > 0);
		assertTrue(urls2.size() > 0);

		assertSame(urls1.size(), urls2.size());

		ArrayList<URL> list1 = new ArrayList<URL>(urls1.size());
		for (URL u : urls1)
			list1.add(u);
		ArrayList<URL> list2 = new ArrayList<URL>(urls2.size());
		for (URL u : urls2)
			list2.add(u);

		assertEquals(list1, list2);
		return urls1;
	}

	protected Set<URL> testGetResourcesDifferentOrder(ClassLoader loader1,
			ClassLoader loader2, String name) {
		Enumeration<URL> e1 = null;
		Enumeration<URL> e2 = null;

		try {
			e1 = loader1.getResources(name);
			e2 = loader2.getResources(name);
		} catch (IOException e) {
			fail("resource " + name + " not found");
		}

		assertNotNull(e1);
		assertNotNull(e2);

		LinkedHashSet<URL> urls1 = new LinkedHashSet<URL>(100);
		LinkedHashSet<URL> urls2 = new LinkedHashSet<URL>(100);

		while (e1.hasMoreElements()) {
			urls1.add(e1.nextElement());
		}

		while (e2.hasMoreElements()) {
			urls2.add(e2.nextElement());
		}

		assertTrue(urls1.size() > 0);
		assertTrue(urls2.size() > 0);

		assertEquals(urls1, urls2);

		ArrayList<URL> list1 = new ArrayList<URL>(urls1.size());
		for (URL u : urls1)
			list1.add(u);
		ArrayList<URL> list2 = new ArrayList<URL>(urls2.size());
		for (URL u : urls2)
			list2.add(u);

		assertFalse(list1.equals(list2));
		return urls1;
	}
}
