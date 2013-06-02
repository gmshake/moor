package tk.blizz.moor.test;

import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.Test;

import tk.blizz.moor.loader.SharedLibClassLoader;

public class SharedLibClassLoaderTest extends ClassLoaderTest {
	@Override
	@Test
	public void testAll() {
		/**
		 * all basic test
		 */
		loader = new SharedLibClassLoader(new URL[] {});
		super.testAll();

		loader = new SharedLibClassLoader(new URL[] {}, null);
		super.testAll();

		loader = new SharedLibClassLoader(new URL[] {}, loader);
		super.testAll();

		testAddClassPath();

	}

	@Test
	public void testAddClassPath() {
		SharedLibClassLoader l = new SharedLibClassLoader();
		l.addClassPath("/usr");
		l.addClassPath("/tmp/shared;/tmp,/");
		l.addClassPath("/tmp/shared;/tmp,/;/Applications:.;/tmp/jetty-6.1.26.jar");
		System.out.println(l.getClassPath());

		try {
			l.addClassPath("----");
			fail("should get IllegalArgumentException");
		} catch (IllegalArgumentException r) {
		}

		try {
			l.addClassPath("/tmp/a.zip");
			fail("should get IllegalArgumentException");
		} catch (IllegalArgumentException r) {
		}
	}
}
