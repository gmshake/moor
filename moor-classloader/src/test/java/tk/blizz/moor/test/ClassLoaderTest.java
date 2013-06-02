package tk.blizz.moor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;

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

		testGetResource("java/lang/Object.class");
		testGetResource(this.getClass().getName().replace('.', '/')
				.concat(".class"));
		testGetResources("java/lang/Object.class");
		testGetResources(this.getClass().getName().replace('.', '/')
				.concat(".class"));
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

	protected void testGetResource(String name) {
		URL url1 = null;
		URL url2 = null;

		url1 = loader.getResource(name);
		url2 = system.getResource(name);

		assertNotNull(url1);
		assertNotNull(url2);
		assertEquals(url1, url2);
	}

	protected void testGetResources(String name) {
		Enumeration<URL> e1 = null;
		Enumeration<URL> e2 = null;

		try {
			e1 = loader.getResources(name);
			e2 = system.getResources(name);
		} catch (IOException e) {
			fail("resource " + name + " not found");
		}

		assertNotNull(e1);
		assertNotNull(e2);

		HashSet<URL> urls1 = new HashSet<URL>(100);
		HashSet<URL> urls2 = new HashSet<URL>(100);

		while (e1.hasMoreElements()) {
			urls1.add(e1.nextElement());
		}

		while (e2.hasMoreElements()) {
			urls2.add(e2.nextElement());
		}

		assertTrue(urls1.size() > 0);
		assertTrue(urls2.size() > 0);
		assertEquals(urls1, urls2);
	}
}
