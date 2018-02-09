package main;

import java.io.Closeable;

import org.apache.log4j.Logger;

public class Utils {

	private static final Logger LOG = Logger.getLogger(Utils.class.getName());
	
	public static void close(Closeable c) {
		try {
			if(c!=null) {
				c.close();
			}
		} catch (Exception e) {
			LOG.error("Error during stream closing.", e);
		}
	}

}
