package pluginlib;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarFile;

public class Plugin<T> {
	
	private File file;
	private String mainClass;
	private Class<T> clazz;
	private T instance;
	
	/**
	 * Tries to load main class from plugin.txt if found
	 * @param file
	 * @throws MainClassNotFoundException
	 * @throws IOException
	 */
	public Plugin(File file) throws Exception, MainClassNotFoundException, IOException {
		this(file, JarUtils.getMainClassFromInfo(new JarFile(file)));		
	}
	
	/**
	 * Adds file to classpath
	 * @param file
	 * @param mainClass
	 * @throws Exception
	 */
	public Plugin(File file, String mainClass) throws Exception {
		this.file = file;
		this.mainClass = mainClass;

		Classpath.addToClassPath(file);
	}
	
	public void load() throws Exception {
		load(null, null);
	}
	
	@SuppressWarnings("unchecked")
	public void load(Class<?>[] classes, Object[] arguments) throws Exception {
		clazz = (Class<T>) Class.forName(mainClass, false, new PluginClassLoader(getClass().getClassLoader()));			
	    
	    Constructor<?> ctor = clazz.getDeclaredConstructor(classes);
		ctor.setAccessible(true);
	    instance = (T) ctor.newInstance(arguments);
	}
	
	/**
	 * Invokes method without arguments
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public Object invoke(String m) throws Exception {
		return invoke(m, null, null);
	}
	
	/**
	 * Invokes method with arguments
	 * @param m
	 * @param classes argument classes
	 * @param arguments argument objects
	 * @return
	 * @throws Exception
	 */
	public Object invoke(String m, Class<?>[] classes, Object[] arguments) throws Exception {
		Method method = clazz.getMethod(m, classes);
		
		return method.invoke(instance, arguments);
	}
	
	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public File getFile() {
		return file;
	}
	
	public T getInstance() {
		return instance;
	}
	
	private class PluginClassLoader extends ClassLoader {

		public PluginClassLoader(ClassLoader parent) {
			super(parent);
		}
		
		@Override
		public URL getResource(String s) {
			try {
				return new URL("jar:file:/" + file.getAbsolutePath() + "!" + s);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			return null;
		}
	}

}
