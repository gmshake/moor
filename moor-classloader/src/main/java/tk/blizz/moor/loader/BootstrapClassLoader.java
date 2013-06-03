package tk.blizz.moor.loader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

public class BootstrapClassLoader extends URLClassLoader {

	public BootstrapClassLoader(URL[] urls) {
		super(urls, null);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return super.loadClass(name, false);
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
