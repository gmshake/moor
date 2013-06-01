package tk.blizz.moor.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

/**
 * 
 * @author zlei.huang@gmail.com
 * 
 */
public class SharedLibClassLoader extends CommonClassLoader {
	private final HashSet<String> classpath;

	public SharedLibClassLoader() {
		this(new URL[] {});
	}

	public SharedLibClassLoader(String path) {
		this(path, null);
	}

	public SharedLibClassLoader(String path, ClassLoader parent) {
		this(new URL[] {}, parent);
		addClassPath(path);
	}

	public SharedLibClassLoader(URL[] urls) {
		this(urls, null);
	}

	public SharedLibClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent, false);
		this.classpath = new HashSet<String>();
	}

	public void addClassPath(String path) {
		if (path == null || path.isEmpty())
			return;
		String[] paths = path.split("[,;:]");

		try {
			for (String s : paths) {
				File file = new File(s);
				if (file.isDirectory()) {
					addURL(file.toURI().toURL());
				} else if (file.isFile()
						&& file.getAbsolutePath().endsWith(".jar")) {
					addURL(new URL("jar", "", file.toURI().toURL()
							.toExternalForm()
							+ "!/"));
				} else {
					throw new IllegalArgumentException("invalid path: " + s);
				}
				this.classpath.add(file.getAbsolutePath());
			}
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public String getClassPath() {
		StringBuilder sb = new StringBuilder(32 * this.classpath.size());
		for (String s : this.classpath) {
			sb.append(s);
			sb.append(':');
		}
		if (sb.length() > 0) {
			return sb.substring(0, sb.length() - 1);
		} else
			return "";
	}

}
