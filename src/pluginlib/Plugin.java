package pluginlib;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.jar.JarFile;

public class Plugin {
	
	private File file;
	private String mainClass;
	private Class<?> clazz;
	private Object instance;
	
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

		Classpath.addToClassPath(file);
	}
	
	public void load() throws Exception {
		load(null, null);
	}
	
	public void load(Class<?>[] classes, Object[] arguments) throws Exception {
		clazz = Class.forName(mainClass, true, getClass().getClassLoader());
				
		Constructor<?> ctor = clazz.getDeclaredConstructor(classes);
	    ctor.setAccessible(true);
	    instance = ctor.newInstance(arguments);
	}
	
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

}
