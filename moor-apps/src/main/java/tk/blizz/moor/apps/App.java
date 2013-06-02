package tk.blizz.moor.apps;

import org.apache.log4j.Logger;

public class App {
	private static final Logger NOTEXIST = null;

	private static void traceClassLoader(Class<?> c) {
		final Logger log = Logger.getLogger(App.class);
		log.info("---------------- trace class loader for " + c.getName()
				+ " -----------------");
		ClassLoader loader = c.getClassLoader();
		do {
			if (loader == null) {
				log.info("bootstrap class loader");
				break;
			} else {
				log.info(loader.toString());
				loader = loader.getParent();
			}
		} while (true);
		log.info("---------------- trace class loader done!!! ------------");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Logger log = null;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		log = Logger.getLogger(App.class);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		log.info("logger's classloader: " + log.getClass().getClassLoader());
		traceClassLoader(App.class);
	}

}
