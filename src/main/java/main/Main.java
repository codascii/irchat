package main;

//import org.apache.log4j.Logger;

import view.ServerFrame;

public class Main {
	//private static final Logger LOG = Logger.getLogger(Main.class.getName());
	
	public boolean t;

	public static void main(String[] args) {
		new ServerFrame();
		/*ServerFrame sf = new ServerFrame();
		Thread t = new Thread(sf);
		t.start();*/
	}
}
