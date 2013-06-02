package tk.blizz.moor.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class AppClassLoader extends SharedLibClassLoader {
	public AppClassLoader() {
		this(null);
	}

	public AppClassLoader(String path) {
		super(path);
		setParentLoaderPriority(true);
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
