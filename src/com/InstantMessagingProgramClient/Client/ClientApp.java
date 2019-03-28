package com.InstantMessagingProgramClient.Client;

import javax.swing.JFrame;

public class ClientApp {

	public static void main(String[] args) {
		Client clientApp;
		clientApp = new Client("127.0.0.1");
		clientApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientApp.startRunning();

	}

}
