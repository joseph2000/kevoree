package org.kevoree.library.javase.kinect;


import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.kevoree.library.javase.kinect.osc.OscServer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@DictionaryType({
		@DictionaryAttribute(name = "address", optional = true, defaultValue = "127.0.0.1"),
		@DictionaryAttribute(name = "port", optional = true, defaultValue = "57111"),
		@DictionaryAttribute(name = "mx", optional = true, defaultValue = ""),
		@DictionaryAttribute(name = "my", optional = true, defaultValue = ""),
		@DictionaryAttribute(name = "mz", optional = true, defaultValue = ""),
		@DictionaryAttribute(name = "ox", optional = true, defaultValue = ""),
		@DictionaryAttribute(name = "oy", optional = true, defaultValue = ""),
		@DictionaryAttribute(name = "oz", optional = true, defaultValue = ""),
		@DictionaryAttribute(name = "w", optional = true, defaultValue = "false"),
		@DictionaryAttribute(name = "r", optional = true, defaultValue = "false"),
		@DictionaryAttribute(name = "f", optional = true, defaultValue = "false"),
		@DictionaryAttribute(name = "k", optional = true, defaultValue = "false"),
		@DictionaryAttribute(name = "q", optional = true, defaultValue = "false")/*,
		@DictionaryAttribute(name = "s", optional = true, defaultValue = "/tmp/osceleton.oni"),
		@DictionaryAttribute(name = "i", optional = true, defaultValue = "/tmp/osceleton.oni")*/
})
@Requires({
		@RequiredPort(name = "osc", type = PortType.MESSAGE)
})
@Library(name = "JavaSE")
@ComponentType
public class Kinect2OSC extends AbstractComponentType {

	private OscServer oscServer;
	private String osceletonExecPath;
	private Process process;

	private int parseAttributeToInt(String name) {
		String property = this.getDictionary().get(name).toString();
		return Integer.parseInt(property);
	}

	private boolean parseAttributeToBoolean(String name) {
		String property = this.getDictionary().get(name).toString();
		return property.equals("true");
	}

	private String parseAttributeToString(String name) {
		return this.getDictionary().get(name).toString();
	}

	@Start
	public void start() {
		try {
			process = Runtime.getRuntime().exec(defineAttributes());
			oscServer = new OscServer(parseAttributeToInt("port"), this);
			oscServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Stop
	public void stop() {
		process.destroy();
		oscServer.killServer();
		oscServer = null;
	}

	public void send(String message) {
		if (isPortBinded("osc")) {
			getPortByName("osc", MessagePort.class).process(message);
		}
		//System.out.println(message);
	}

	private String[] defineAttributes() {
		List<String> attributes = new ArrayList<String>();
		attributes.add(getOSCeletonExec());
		attributes.add("-p");
		attributes.add("" + parseAttributeToInt("port"));
		attributes.add("-a");
		attributes.add("" + parseAttributeToString("address"));
		try {
			int value = parseAttributeToInt("mx");
			attributes.add("-mx");
			attributes.add(""+value);
		} catch (NumberFormatException e) {}
		try {
			int value = parseAttributeToInt("my");
			attributes.add("-my");
			attributes.add(""+value);
		} catch (NumberFormatException e) {}
		try {
			int value = parseAttributeToInt("mz");
			attributes.add("-mz");
			attributes.add(""+value);
		} catch (NumberFormatException e) {}
		try {
			int value = parseAttributeToInt("ox");
			attributes.add("-ox");
			attributes.add(""+value);
		} catch (NumberFormatException e) {}
		try {
			int value = parseAttributeToInt("oy");
			attributes.add("-oy");
			attributes.add(""+value);
		} catch (NumberFormatException e) {}
		try {
			int value = parseAttributeToInt("oz");
			attributes.add("-oz");
			attributes.add(""+value);
		} catch (NumberFormatException e) {}

		if (parseAttributeToBoolean("w")) {
			attributes.add("-w");
		}
		if (parseAttributeToBoolean("q")) {
			attributes.add("-q");
		}
		if (parseAttributeToBoolean("r")) {
			attributes.add("-r");
		}
		if (parseAttributeToBoolean("f")) {
			attributes.add("-f");
		}
		if (parseAttributeToBoolean("k")) {
			attributes.add("-k");
		}

		return attributes.toArray(new String[attributes.size()]);
	}

	private String getOSCeletonExec() {
		try {
			if (osceletonExecPath == null) {
				File osceletonExec = File.createTempFile("osceleton", "");
				osceletonExec.setExecutable(true);
				OutputStream osceletonStream = new FileOutputStream(osceletonExec);
				InputStream stream = getStream();

				byte[] bytes = new byte[1024];
				int length = 0;
				while ((length = stream.read(bytes)) > -1) {
					osceletonStream.write(bytes, 0, length);
				}
				stream.close();
				osceletonStream.flush();
				osceletonStream.close();
				osceletonExecPath = osceletonExec.getAbsolutePath();
			}
			return osceletonExecPath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private InputStream getStream() {
		String resourcePath = foundOSPath() + "/osceleton";
		return this.getClass().getClassLoader().getResourceAsStream(resourcePath);
	}

	private String foundOSPath() {
		if (isUnix()) {
			if (!is64()) {
				return "linux/x86";
			} else if (is64()) {
				return "linux/x86_64";
			}
		} else if (isMac()) {
			if (!is64()) {
				return "macosx/x86";
			} else if (is64()) {
				return "macosx/x86_64";
			}
		} else if (isWindows()) {
			if (!is64()) {
				return "windows/x86";
			} else if (is64()) {
				return "windows/x86_64";
			}
		}
		return ".";
	}

	public boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		//windows
		return (os.indexOf("win") >= 0);
	}

	public boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		//Mac
		return (os.indexOf("mac") >= 0);
	}

	public boolean isUnix() {
		String os = System.getProperty("os.name").toLowerCase();
		//linux or unix
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}

	public boolean is64() {
		String os = System.getProperty("os.arch").toLowerCase();
		return (os.indexOf("64") >= 0);
	}
}