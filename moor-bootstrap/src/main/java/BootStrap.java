import tk.blizz.moor.loader.MoorClassLoader;

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
	 */
	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		MoorClassLoader loader = new MoorClassLoader();

		Runnable moor = (Runnable) loader.loadClass("tk.blizz.moor.Main")
				.newInstance();

		Thread boot = new Thread(new ThreadGroup("moor"), moor, "bootstrap");
		boot.setContextClassLoader(loader);
		boot.start();
	}
}
