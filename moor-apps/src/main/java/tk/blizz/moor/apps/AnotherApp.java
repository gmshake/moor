package tk.blizz.moor.apps;

import org.apache.log4j.Logger;

public class AnotherApp {
	private static final Logger log = Logger.getLogger(AnotherApp.class);

	private static void traceClassLoader(Class<?> c) {
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
		log.info("logger's classloader: " + log.getClass().getClassLoader());
		traceClassLoader(AnotherApp.class);
	}
}
