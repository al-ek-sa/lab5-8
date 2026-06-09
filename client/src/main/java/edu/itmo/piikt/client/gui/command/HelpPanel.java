package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HelpPanel extends JPanel {
	private final String username;
	private final LocaleManager lm;
	private final JTextArea helpTextArea;
	private final JLabel titleLabel;

	public HelpPanel(String username) {
		this.username = username;
		this.lm = LocaleManager.getInstance();

		setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		titleLabel = new JLabel();
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
		add(titleLabel, BorderLayout.NORTH);

		helpTextArea = new JTextArea();
		helpTextArea.setBackground(Color.BLACK);
		helpTextArea.setForeground(new Color(200, 200, 200));
		helpTextArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		helpTextArea.setEditable(false);
		helpTextArea.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

		JScrollPane scrollPane = new JScrollPane(helpTextArea);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setBackground(Color.BLACK);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		lm.addLocaleChangeListener(() -> {
			updateTitle();
			loadHelp();
		});
		updateTitle();
		loadHelp();
	}

	private void updateTitle() {
		titleLabel.setText(lm.getString("command.help"));
	}

	private void loadHelp() {
		helpTextArea.setText(lm.getString("message.loading"));

		new Thread(() -> {
			try {
				ClientCommand command = ClientCommand.builder().nameCommand("help").user(username).build();

				ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

				SwingUtilities.invokeLater(() -> {
					if (response != null && response.execution()) {
						StringBuilder formattedHelp = new StringBuilder();

						if (response.data() instanceof List) {
							List<String> commands = response.data();
							for (String cmd : commands) {
								formattedHelp.append("• ").append(cmd).append("\n\n");
							}
						} else if (response.message() != null && !response.message().isEmpty()) {
							String msg = response.message();
							if (msg.startsWith("HELP: ")) {
								String commandsStr = msg.substring(6);
								String[] commands = commandsStr.split(", ");
								for (String cmd : commands) {
									formattedHelp.append("• ").append(cmd).append("\n\n");
								}
							} else {
								formattedHelp.append(msg);
							}
						}

						if (formattedHelp.isEmpty()) {
							helpTextArea.setText(lm.getString("message.no_data"));
						} else {
							helpTextArea.setText(formattedHelp.toString());
						}
					} else {
						String errorMsg = response != null ? response.message() : lm.getString("error.load");
						helpTextArea.setText(lm.getString("error.prefix") + errorMsg);
					}
				});
			} catch (Exception ex) {
				SwingUtilities.invokeLater(
						() -> helpTextArea.setText(lm.getString("error.connection") + ": " + ex.getMessage()));
			}
		}).start();
	}
}
