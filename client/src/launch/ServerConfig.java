package launch;

public class ServerConfig {
	
	private int port;
	private String hostname;
	private String path;
	
	public ServerConfig(String hostname, int port, String path) {
		this.port = port;
		this.hostname = hostname;
		this.path = path;
	}

	public int getPort() {
		return port;
	}

	public String getHostname() {
		return hostname;
	}

	public String getPath() {
		return path;
	}
	
}
