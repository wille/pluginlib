package pluginlib.example;

public class ExamplePlugin {
	
	private String s;
	
	public ExamplePlugin() {
		this("Example Plugin");
	}
	
	public ExamplePlugin(String s) {
		this.s = s;
	}
	
	public void printString() {
		System.out.println(s);
	}

}
