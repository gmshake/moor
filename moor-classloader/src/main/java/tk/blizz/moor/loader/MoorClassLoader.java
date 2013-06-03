package tk.blizz.moor.loader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

public class MoorClassLoader extends CommonClassLoader {
	private static URL fileToURL(String fileName) {
		try {
			return new File(fileName).toURI().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public MoorClassLoader() {
		super(new URL[] { fileToURL("/tmp/moor/bin/moor.jar"),
				fileToURL("/tmp/moor/bin/log4j.jar") });
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return super.loadClass(name);
	}

	@Override
	public URL getResource(String name) {
		return super.getResource(name);
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return super.getResources(name);
	}
}
