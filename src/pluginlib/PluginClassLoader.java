package pluginlib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class PluginClassLoader extends ClassLoader {
	
	private Map<String, byte[]> classes = new HashMap<String, byte[]>();
	

	public PluginClassLoader(ClassLoader parent, JarInputStream jis) {
		super(parent);
		this.loadResources(jis);
	}

	/**
	 * Returns null (for now) as we will get from Controller.jar if we call this
	 */
	@Override
	public InputStream getResourceAsStream(String resource) {
		return null;
	}

	/**
	 * Returns null (for now) as we will get from Controller.jar if we call this
	 */
	@Override
	public URL getResource(String resource) {
		return null;
	}

	/**
	 * Manually changing plugin dir, make sure it's correct
	 * @param folder
	 * @param resource
	 * @return FileInputStream from file in plugin folder
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public InputStream getPluginFile(String folder, String resource) throws FileNotFoundException, IOException {
		return new FileInputStream(new File("plugins/" + folder, resource));
	}

	/**
	 * 
	 * @param folder
	 * @param resource
	 * @return URL to file in plugin folder
	 * @throws MalformedURLException
	 */
	public URL getPluginUrl(String folder, String resource) throws MalformedURLException {
		return new URL("plugins/" + folder + "/" + resource);
	}
	
	
	/**
	 * Finds the class in the map
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] data = getClassData(name);
		
		if (data != null) {
			return defineClass(name, data, 0, data.length, this.getClass().getProtectionDomain());
		} else {
			throw new ClassNotFoundException(name);
		}
	}
	
	/**
	 * Loads classes into memory from JAR
	 * @param stream
	 */
	public void loadResources(JarInputStream stream) {
		byte[] buffer = new byte[1024];

		int count;

		try {
			JarEntry entry = null;
			while ((entry = stream.getNextJarEntry()) != null) {
				if (entry.getName().toLowerCase().endsWith(".class")) {
					int size = (int) entry.getSize();
	
					ByteArrayOutputStream out = new ByteArrayOutputStream(size == -1 ? 1024 : size);
	
					while ((count = stream.read(buffer)) != -1) {
						out.write(buffer, 0, count);
					}
	
					out.close();
	
					byte[] array = out.toByteArray();
				
					classes.put(getClassName(entry.getName()), array);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets class name from file name
	 * @param fileName
	 * @return
	 */
	public static String getClassName(String fileName) {
		return fileName.substring(0, fileName.length() - 6).replace('/', '.');
	}
	
	/**
	 * Gets and removes the data from map
	 * @param name
	 * @return
	 */
	public byte[] getClassData(String name) {
		byte[] b = classes.get(name);
		classes.remove(name);
		return b;
	}
}
