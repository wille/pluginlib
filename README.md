# pluginlib

Simple Java library for basic plugin functionality

## Usage

See package ```pluginlib.example```

## Example

### Abstract Plugin
```java
public abstract class AbstractPlugin {
	
	protected final String name;
	
	public AbstractPlugin(String name) {
		this.name = name;
	}
	
	/**
	 * Returns unique name for this plugin
	 * @return
	 */
	public final String getName() {
		return this.name;
	}

}
```
### Example Plugin

```java
public class ExamplePlugin extends AbstractPlugin {

	public ExamplePlugin() {
		super("Example"); // Sets name to Example
	}

}
```

### Load Plugins

```java
PluginLoader<ExamplePlugin> loader = new PluginLoader<ExamplePlugin>(new File("plugins/")); // Load all plugins found in ./plugins/ folder
loader.loadPlugins();
```
