package com.InstantMessagingProgramClient.Client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String ipServer;
	private Socket connection;
	
	public Client(String host) {
		super("Anthony's IMP Client");
		ipServer = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
				);
			add(userText, BorderLayout.NORTH);
			chatWindow = new JTextArea();
			add(new JScrollPane(chatWindow), BorderLayout.CENTER);
			setSize(300,150);
			setVisible(true);
	}
	
	// start the server
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		} catch (EOFException eofException) {
			showMessage("\n Client terminated connection");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			closeAll();
		}
	}
	
	// connect to server
	private void connectToServer() throws IOException {
		showMessage("Trying to connect... \n");
		connection = new Socket(InetAddress.getByName(ipServer), 6789);
		showMessage("Connected to: " + connection.getInetAddress().getHostName());
	}
	
	//setup streams to send/receive messages
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are set! \n");
	}
	
	//close streams and sockets
	private void closeAll() {
		showMessage("\n Closing down connections...");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	//while chatting with server
	private void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			} catch (ClassNotFoundException c) {
				showMessage("\n Unable to read object being sent.");
			}
		} while(!message.equals("SERVER - END"));
	}
	
	//send message to server
	private void sendMessage(String message) {
		try {
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\n CLIENT - " + message);
		} catch(IOException e) {
			chatWindow.append("Something went wrong!");
		}
	}
	
	//updates chatWindow
		private void showMessage(final String text) {
			SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					chatWindow.append(text);
				}
			}
			);
		}
		
		// let user type in chatbox
		private void ableToType (final boolean trueOrFalse) {
			SwingUtilities.invokeLater(
					new Runnable() {
						public void run() {
							userText.setEditable(trueOrFalse);
						}
					}
					);
		}

}
