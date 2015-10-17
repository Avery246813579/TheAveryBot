package com.frostbyte.theaverybot.irc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import com.frostbyte.theaverybot.irc.server.IConnection;
import com.frostbyte.theaverybot.irc.server.IQueue;
import com.frostbyte.theaverybot.irc.server.ReplyConstants;
import com.frostbyte.theaverybot.irc.server.UnverifiedSSL;
import com.frostbyte.theaverybot.irc.threads.InputThread;
import com.frostbyte.theaverybot.irc.threads.OutputThread;

public class Irc implements ReplyConstants {
	/** Threads **/
	public InputThread inputThread = null;
	public OutputThread outputThread = null;
	public boolean running = true;

	/** Server **/
	private InetAddress address;
	private IQueue queue = new IQueue();

	/** Chars **/
	private String _charset = null;
	private String _channelPrefixes = "#&+!";

	/** Configuration **/
	private boolean verbose = false;
	private boolean debug = false;
	private String username;

	/** Lists **/
	private List<User> users = new ArrayList<User>();

	public final synchronized void connect(String host, int port, String password) throws Exception {
		IConnection connection = new IConnection(host);
		connection.setPort(port);
		connection.setPassword(password);
		connect(connection);
	}

	public final synchronized void connect(IConnection connection) throws Exception {
		IConnection dupConnection = connection.clone();
		connection = dupConnection;

		if (isConnected()) {
			throw new IOException("Bot already in irc channel");
		}

		Socket socket = null;

		if (dupConnection.isUseSLL()) {
			try {
				SocketFactory factory;
				if (dupConnection.isVerifySSL()) {
					factory = SSLSocketFactory.getDefault();
				} else {
					SSLContext context = UnverifiedSSL.getUnverifiedSSLContext();
					factory = context.getSocketFactory();
				}

				socket = factory.createSocket(dupConnection.getServer(), dupConnection.getPort());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			socket = new Socket(dupConnection.getServer(), dupConnection.getPort());
		}

		log("-> Connected to server <-");

		address = socket.getLocalAddress();

		InputStreamReader inputStreamReader = null;
		OutputStreamWriter outputStreamWriter = null;

		if (getEncoding() != null) {
			inputStreamReader = new InputStreamReader(socket.getInputStream(), getEncoding());
			outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), getEncoding());
		} else {
			inputStreamReader = new InputStreamReader(socket.getInputStream());
			outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
		}

		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

		if (dupConnection.getPassword() != null && !dupConnection.getPassword().equalsIgnoreCase("")) {
			OutputThread.sendRawLine(this, bufferedWriter, "PASS " + dupConnection.getPassword());
		}

		String nick = getUsername();
		OutputThread.sendRawLine(this, bufferedWriter, "NICK " + nick);
		OutputThread.sendRawLine(this, bufferedWriter, "USER " + nick + " 8 * :" + nick);

		inputThread = new InputThread(this, socket, bufferedReader, bufferedWriter);

		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			handleLine(line);

			int firstSpace = line.indexOf(" ");
			int secondSpace = line.indexOf(" ", firstSpace + 1);
			if (secondSpace >= 0) {
				String code = line.substring(firstSpace + 1, secondSpace);

				if (code.equals("004")) {
					break;
				} else if (code.equals("433")) {
					socket.close();
					inputThread = null;
					throw new InternalError("Nick already in use!");
				} else if (code.startsWith("5") || code.startsWith("4")) {
					socket.close();
					inputThread = null;
					throw new InternalError("Could not log to IRC server: " + line);
				}
			}
		}

		log("-> Logged onto server <-");

		socket.setSoTimeout(5 * 60 * 1000);
		inputThread.start();

		if (outputThread == null) {
			outputThread = new OutputThread(this, queue);
			outputThread.start();
		}

		onConnect();
	}

	public void handleLine(String line) {
		log(line);

		if (line.startsWith(":jtv MODE #") && line.contains("+o")) {
			moderatorJoin(line.split(" ")[4]);
		}

		if (line.startsWith(":jtv MODE #") && line.contains("-o")) {
			moderatorLeave(line.split(" ")[4]);
		}

		if (line.contains("The moderators of this room are")) {
			boolean first = false;
			for (String string : (line.split(":")[3].split(" "))) {
				if (first) {
					findUser(string.substring(0, string.length() - 1)).setMod(true);
				} else {
					first = true;
				}
			}
		}

		if (line.contains(":SPECIALUSER") && line.contains("subscriber")) {
			findUser(line.split(" ")[4]).setSub(true);
		}

		if (line.contains("twitchnotify!twitchnotify@twitchnotify.tmi.twitch.tv")) {
			try {
				String[] split = line.split(" ");
				findUser(split[2].substring(1)).setSub(true);
				onSubscribe(split[2].substring(1), split[3].substring(1));
			} catch (Exception ex) {

			}
		}

		if (line.startsWith("PING ")) {
			onServerPing(line.substring(5));
			return;
		}

		String sourceNick = "";
		String sourceLogin = "";
		String sourceHostname = "";

		StringTokenizer tokenizer = new StringTokenizer(line);
		String senderInfo = tokenizer.nextToken();
		String command = tokenizer.nextToken();
		String target = null;

		int exclamation = senderInfo.indexOf("!");
		int at = senderInfo.indexOf("@");
		if (senderInfo.startsWith(":")) {
			if (exclamation > 0 && at > 0 && exclamation < at) {
				sourceNick = senderInfo.substring(1, exclamation);
				sourceLogin = senderInfo.substring(exclamation + 1, at);
				sourceHostname = senderInfo.substring(at + 1);
			} else {

				if (tokenizer.hasMoreTokens()) {
					String token = command;

					int code = -1;
					try {
						code = Integer.parseInt(token);
					} catch (NumberFormatException e) {
					}

					if (code != -1) {
						if (code == RPL_NAMREPLY) {
							String errorStr = token;
							String response = line.substring(line.indexOf(errorStr, senderInfo.length()) + 4, line.length());
							StringTokenizer tokenizer1 = new StringTokenizer(response.substring(response.indexOf(" :") + 2));
							while (tokenizer1.hasMoreTokens()) {
								String nick = tokenizer1.nextToken();
								String prefix = "";
								if (nick.startsWith("@")) {
									// User is an operator in this channel.
									prefix = "@";
								} else if (nick.startsWith("+")) {
									// User is voiced in this channel.
									prefix = "+";
								} else if (nick.startsWith(".")) {
									// Some wibbly status I've never seen
									// before...
									prefix = ".";
								}
								nick = nick.substring(prefix.length());
								addUser(new User("", nick));
							}
							return;
						}
					} else {
						sourceNick = senderInfo;
						target = token;
					}
				} else {
					return;
				}

			}
		}

		command = command.toUpperCase();
		if (sourceNick.startsWith(":")) {
			sourceNick = sourceNick.substring(1);
		}
		if (target == null) {
			target = tokenizer.nextToken();
		}
		if (target.startsWith(":")) {
			target = target.substring(1);
		}

		if (command.equals("PRIVMSG") && _channelPrefixes.indexOf(target.charAt(0)) >= 0) {
			if (running) {
				onMessage(target, sourceNick, sourceLogin, sourceHostname, line.substring(line.indexOf(" :") + 2));
			}
		} else if (command.equals("PART")) {
			removeUser(new User("", sourceNick));
		} else if (command.equals("JOIN")) {
			addUser(new User("", sourceNick));
		}
	}

	public void joinChannel(String channel) {
		this.sendRawLine("JOIN " + channel);

		if (findUser(getUsername()) != null) {
			findUser(getUsername()).setMod(true);
		}
	}

	public final void sendMessage(String target, String message) {
		queue.add("PRIVMSG " + target + " :" + message);
	}

	public void moderatorJoin(String user) {
		findUser(user).setMod(true);
		addUser(new User("", user));
	}

	public void moderatorLeave(String user) {
		findUser(user).setMod(false);
		removeUser(new User("", user));
	}

	public void addUser(User user) {
		boolean added = false;
		for (User tuser : users) {
			if (tuser.getNick().toLowerCase().equalsIgnoreCase(user.getNick().toLowerCase())) {
				added = true;
				break;
			}
		}

		if (!added) {
			debug("Join: " + user.getNick());
			users.add(user);
		}
	}

	public void removeUser(User user) {
		boolean added = false;
		for (User tuser : users) {
			if (getUsername().toLowerCase().equalsIgnoreCase(tuser.getNick().toLowerCase())) {
				added = true;
				break;
			}
		}

		if (added) {
			debug("Part: " + getUsername());
			users.remove(user);
		}
	}

	public User findUser(String username) {
		for (User user : users) {
			if (user.getNick().toLowerCase() == username.toLowerCase()) {
				return user;
			}
		}

		return null;
	}

	public final synchronized void sendRawLine(String line) {
		if (isConnected()) {
			inputThread.sendRawLine(line);
		}
	}

	public int getMaxLineLength() {
		return InputThread.MAX_LINE_LENGTH;
	}

	public long getMessageDelay() {
		return 1000;
	}

	public String getEncoding() {
		return _charset;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void log(String line) {
		if (verbose) {
			System.out.println(System.currentTimeMillis() + ">> " + line);
		}
	}

	public void debug(String line) {
		if (debug) {
			System.out.println(line);
		}
	}

	public final synchronized boolean isConnected() {
		return inputThread != null && inputThread.isConnected();
	}

	protected void onServerPing(String response) {
		sendRawLine("PONG " + response);
	}

	/** Protected Stuff **/
	public void onDisconnect() {
	}

	protected void onConnect() {
	}

	protected void onSubscribe(String user, String string) {
	}

	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
	}

	protected void onJoin(String channel, String user) {
	}

	protected void onLeave(String channel, String user) {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
