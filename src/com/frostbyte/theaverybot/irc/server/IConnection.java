package com.frostbyte.theaverybot.irc.server;

public class IConnection {
	private String server, password;
	private boolean useSLL, verifySSL;
	private int port = 6667;
	
	public IConnection(String server){
		this.server = server;
	}
	
	public IConnection clone(){
		IConnection connection = new IConnection(server);
		connection.setPassword(password);
		connection.setUseSLL(useSLL);
		connection.setVerifySSL(verifySSL);
		connection.setPort(port);
		
		return connection;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isUseSLL() {
		return useSLL;
	}

	public void setUseSLL(boolean useSLL) {
		this.useSLL = useSLL;
	}

	public boolean isVerifySSL() {
		return verifySSL;
	}

	public void setVerifySSL(boolean verifySSL) {
		this.verifySSL = verifySSL;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
