package tk.blizz.moor;

import org.apache.log4j.Logger;

public class MonitorStarter implements Runnable {
	private static final Logger log = Logger.getLogger(MonitorStarter.class);

	@Override
	public void run() {
		log.info("start...");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		log.info("exit...");
	}

}
