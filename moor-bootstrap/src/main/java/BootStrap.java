import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * bootstrap class
 * 
 * @author zlei.huang@gmail.com
 * 
 */
public class BootStrap {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			MalformedURLException {
		System.out.println("bootstrap start ...");
		String home = System.getProperty("moor.home");
		if (home == null) {
			home = "/tmp/moor/";
			System.setProperty("moor.home", home);
			System.err
					.println("moor system property moor.home not set, use /tmp/moor");
		}
		File moorHome = new File(home);

		if (!moorHome.isDirectory()) {
			System.err.println(moorHome.getAbsolutePath()
					+ " not exists, abort!");
			System.exit(-1);
		}

		URL[] urls = new URL[] {
				new File(moorHome, "/lib/classloader.jar").toURI().toURL(),
				new File(moorHome, "/lib/moor.jar").toURI().toURL(),
				new File(moorHome, "/lib/log4j-boot.jar").toURI().toURL() };
		BootstrapClassLoader loader = new BootstrapClassLoader(urls);

		Runnable moor = (Runnable) loader.loadClass("tk.blizz.moor.Main")
				.newInstance();

		Thread boot = new Thread(new ThreadGroup("moor"), moor, "main");
		boot.start();
	}

	private static class BootstrapClassLoader extends URLClassLoader {
		private BootstrapClassLoader(URL[] urls) {
			super(urls, null);
		}
	}
}
