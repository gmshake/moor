package tk.blizz.moor;

import java.util.Random;

import org.apache.log4j.Logger;

public class MonitorStarter implements Runnable {
	// private static final Logger log = Logger.getLogger(MonitorStarter.class);

	@Override
	public void run() {
		final Logger log = Logger.getLogger(this.getClass());
		log.info("start...");

		log.debug("monitor classloader: " + this.getClass().getClassLoader());
		log.info("MonitorStarter.class 's loader: "
				+ MonitorStarter.class.getClassLoader());

		Random r = new Random();
		try {
			Thread.sleep(100 + r.nextInt(200));
		} catch (InterruptedException e) {
		}

		log.info("exit...");
	}

}
