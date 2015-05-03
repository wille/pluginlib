package pluginlib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class Plugin {
	
	private File file;
	private JarFile jarfile;
	private String mainClass;
	private Class<?> clazz;
	private Object instance;
	
	public Plugin(File file) throws MainClassNotFoundException, IOException {
		try {
			this.file = file;
			this.jarfile = new JarFile(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			this.mainClass = JarUtils.getMainClassFromInfo(jarfile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void load() throws Exception {
		load(null, null);
	}
	
	public void load(Class<?>[] classes, Object[] arguments) throws Exception {
		clazz = Class.forName(mainClass, true, new PluginClassLoader(new JarInputStream(new FileInputStream(file))));
				
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
