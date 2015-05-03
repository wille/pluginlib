package pluginlib.example;

import java.io.File;
import pluginlib.Plugin;

public class Loader {
	
	public static void main(String[] args) throws Exception {
		Plugin plugin = new Plugin(new File("example.jar"));
		
		plugin.setMainClass("pluginlib.example.ExamplePlugin");
		
		plugin.load();
		
		plugin.invoke("printString", null, null);
		
		plugin.load(new Class[] { String.class }, new Object[] { "Test" });
		
		plugin.invoke("printString", null, null);
	}

}
