package edu.itmo.piikt.client.network;

import edu.itmo.piikt.common.sc.Client;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.common.util.DS;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@Data
@NoArgsConstructor
public class Network implements Client {
	private static final int HEADER_SIZE = 8;
	private static final int SO_TIMEOUT = 5000;
	private static final int CONNECT_ATTEMPTS = 30;
	private static final String HOST = System.getenv().getOrDefault("SERVER_HOST", "localhost");
	private static final int PORT = 6654;

	private SocketChannel socketChannel;
	private volatile boolean isClosing = false;
	private volatile boolean shouldStop = false;
	private volatile boolean isConnectedFlag = false;
	private Thread connectThread;
	private Thread commandProcessorThread;
	private boolean userWantsToExit = false;
	private final BlockingQueue<PendingCommand> commandQueue = new LinkedBlockingQueue<>();
	private ServerResponse pendingResponse = null;
	private final Object responseLock = new Object();

	private static class PendingCommand {
		final ClientCommand command;
		final long timestamp;

		PendingCommand(ClientCommand command) {
			this.command = command;
			this.timestamp = System.currentTimeMillis();
		}
	}

	@Override
	public void connect() {
		shouldStop = false;
		isConnectedFlag = false;
		userWantsToExit = false;

		connectThread = new Thread(this::connectWithTimeout);
		connectThread.setDaemon(true);
		connectThread.start();

		commandProcessorThread = new Thread(this::processCommandQueue);
		commandProcessorThread.setDaemon(true);
		commandProcessorThread.start();
	}

	private void connectWithTimeout() {
		for (int attempt = 1; attempt <= CONNECT_ATTEMPTS && !shouldStop && !userWantsToExit; attempt++) {
			try {
				socketChannel = SocketChannel.open();
				socketChannel.configureBlocking(true);
				socketChannel.socket().setSoTimeout(SO_TIMEOUT);
				socketChannel.connect(new InetSocketAddress(HOST, PORT));
				isConnectedFlag = true;
				return;
			} catch (IOException e) {
				isConnectedFlag = false;
				if (attempt >= CONNECT_ATTEMPTS) {
					showReconnectDialog();
					attempt = 0;
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

	private void processCommandQueue() {
		while (!isClosing && !shouldStop) {
			try {
				PendingCommand pending = commandQueue.poll(1, TimeUnit.SECONDS);
				if (pending != null && isConnectedFlag && socketChannel != null && socketChannel.isConnected()) {
					sendCommandInternal(pending.command);
				} else if (pending != null) {
					synchronized (responseLock) {
						pendingResponse = ServerResponse.error("Сервер недоступен");
						responseLock.notifyAll();
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

	private void sendCommandInternal(ClientCommand command) {
		try {
			ByteBuffer writeBuffer = DS.serializeWithSize(command);
			socketChannel.write(writeBuffer);
			ByteBuffer headerBuffer = ByteBuffer.allocate(HEADER_SIZE);
			int headerBytes = 0;
			while (headerBytes < HEADER_SIZE) {
				int read = socketChannel.read(headerBuffer);
				if (read == -1) {
					synchronized (responseLock) {
						pendingResponse = ServerResponse.error("нет соединения");
						responseLock.notifyAll();
					}
					return;
				}
				headerBytes += read;
			}
			headerBuffer.flip();
			long responseSize = headerBuffer.getLong();

			ByteBuffer dataBuffer = ByteBuffer.allocate((int) responseSize);
			int dataBytes = 0;
			while (dataBytes < responseSize) {
				int read = socketChannel.read(dataBuffer);
				if (read == -1) {
					synchronized (responseLock) {
						pendingResponse = ServerResponse.error("нет соединения");
						responseLock.notifyAll();
					}
					return;
				}
				dataBytes += read;
			}
			dataBuffer.flip();
			ServerResponse response = DS.deserialize(dataBuffer, ServerResponse.class);

			synchronized (responseLock) {
				pendingResponse = response;
				responseLock.notifyAll();
			}
		} catch (Exception e) {
			synchronized (responseLock) {
				pendingResponse = ServerResponse.error("Ошибка отправки");
				responseLock.notifyAll();
			}
		}
	}

	private void showReconnectDialog() {
		SwingUtilities.invokeLater(() -> {
			if (userWantsToExit)
				return;

			JFrame parent = null;
			for (Frame frame : JFrame.getFrames()) {
				if (frame.isVisible()) {
					parent = (JFrame) frame;
					break;
				}
			}

			JDialog dialog = new JDialog(parent, "Ошибка подключения", true);
			dialog.setSize(400, 180);
			dialog.setLocationRelativeTo(parent);
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

			JPanel panel = new JPanel(new BorderLayout(10, 10));
			panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

			JTextArea message = new JTextArea("Сервер недоступен.\n" + "Хотите выйти из приложения?");
			message.setEditable(false);
			message.setOpaque(false);
			message.setFont(new Font("Arial", Font.PLAIN, 14));
			panel.add(message, BorderLayout.NORTH);

			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
			JButton exitButton = new JButton("Выйти");
			JButton waitButton = new JButton("Продолжить ожидание");

			exitButton.setBackground(new Color(48, 48, 48));
			exitButton.setForeground(Color.WHITE);
			exitButton.setFocusPainted(false);

			waitButton.setBackground(new Color(48, 48, 48));
			waitButton.setForeground(Color.WHITE);
			waitButton.setFocusPainted(false);

			buttonPanel.add(exitButton);
			buttonPanel.add(waitButton);
			panel.add(buttonPanel, BorderLayout.SOUTH);

			dialog.add(panel);

			exitButton.addActionListener(e -> {
				dialog.dispose();
				userWantsToExit = true;
				shouldStop = true;
				System.exit(0);
			});

			waitButton.addActionListener(e -> {
				dialog.dispose();
				connect();
			});

			dialog.setVisible(true);
		});
	}

	@Override
	public ServerResponse send(ClientCommand clientResponse) throws Exception {
		if (isClosing || shouldStop || userWantsToExit) {
			return ServerResponse.error("Сервер недоступен");
		}

		synchronized (responseLock) {
			pendingResponse = null;
		}

		commandQueue.offer(new PendingCommand(clientResponse));

		synchronized (responseLock) {
			long startTime = System.currentTimeMillis();
			while (pendingResponse == null && (System.currentTimeMillis() - startTime) < 35000) {
				responseLock.wait(1000);
			}
			if (pendingResponse != null) {
				return pendingResponse;
			} else {
				return ServerResponse.error("Таймаут ожидания ответа");
			}
		}
	}

	@Override
	public void close() {
		isClosing = true;
		shouldStop = true;
		if (connectThread != null) {
			connectThread.interrupt();
		}
		if (commandProcessorThread != null) {
			commandProcessorThread.interrupt();
		}
		if (socketChannel != null) {
			try {
				socketChannel.close();
			} catch (IOException e) {
				//
			}
		}
	}
}
