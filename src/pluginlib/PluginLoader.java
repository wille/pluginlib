package pluginlib;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginLoader<T> {
	
	private final List<Plugin<T>> plugins = new ArrayList<Plugin<T>>();
	
	private File file;
	
	/**
	 * Creates new PluginLoader, sets default plugin directory to ./plugins/
	 */
	public PluginLoader() {
		this(new File("plugins/"));
	}
	
	/**
	 * Creates new PluginLoader
	 * @param file default directory
	 */
	public PluginLoader(File file) {
		this.file = file;
	}
	
	/**
	 * Load all plugins from detected directory
	 * 
	 * If plugins are loaded, will reload all plugins
	 * 
	 * Only loads files ending with .jar
	 * @throws Exception
	 */
	public void loadPlugins() throws Exception {
		plugins.clear();
		
		File files[] = file.listFiles();
		
		if (files != null) {
			for (File f : files) {
				if (f.isFile() && f.getName().toLowerCase().endsWith(".jar")) {
					try {
						load(f);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Load plugin from file
	 * @param file
	 * @throws Exception
	 */
	public void load(File file) throws Exception {	
		Classpath.addToClasspath(file);

		Plugin<T> plugin = new Plugin<T>(file);
		
		plugin.load();
		
		plugins.add(plugin);
	}
	
	/**
	 * Returns plugins
	 * @return
	 */
	public List<Plugin<T>> getRawPlugins() {
		return plugins;
	}
	
	/**
	 * Returns loaded plugin main classes
	 * @return
	 */
	public List<T> getPlugins() {
		List<T> list = new ArrayList<T>();
		
		for (Plugin<T> plugin : plugins) {
			list.add(plugin.getInstance());
		}
		
		return list;
	}

}
