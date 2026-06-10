package edu.itmo.piikt.client.gui.command;

import edu.itmo.piikt.client.gui.localization.LocaleManager;
import edu.itmo.piikt.client.gui.ss.MainAppPanel;
import edu.itmo.piikt.client.manager.GuiCommandSender;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.models.Coordinates;
import edu.itmo.piikt.common.models.Status;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.common.models.OrganizationType;
import edu.itmo.piikt.common.models.Address;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class AnimationPanel extends JPanel {
	private final MainAppPanel parent;
	private final String currentUser;
	private final LocaleManager lm;
	private final JLabel titleLabel;
	private final List<Gear> gears;
	private final Random random;
	private final List<Image> gearImages;
	private final ConcurrentHashMap<String, GearAnimation> activeAnimations;
	private List<Worker> workers;
	private final JPanel thumbnailPanel;
	private final GearCanvas gearCanvas;
	private double offsetX = 0;
	private double offsetY = 0;
	private double zoom = 1.0;
	private final double minZoom = 0.1;
	private final double maxZoom = 5.0;
	private double worldMinX = 0;
	private double worldMaxX = 1000;
	private double worldMinY = 0;
	private double worldMaxY = 1000;
	private boolean hasCoordinates = false;
	private int dragStartX, dragStartY;
	private double dragStartOffsetX, dragStartOffsetY;
	private boolean dragging = false;
	private int viewportWidth = 1000;
	private int viewportHeight = 700;
	private String highlightedGearId = null;
	private final Timer highlightTimer;

	public AnimationPanel(MainAppPanel parent, String username) {
		this.parent = parent;
		this.currentUser = username;
		this.lm = LocaleManager.getInstance();
		this.gears = new ArrayList<>();
		this.random = new Random();
		this.gearImages = new ArrayList<>();
		this.activeAnimations = new ConcurrentHashMap<>();
		this.workers = new ArrayList<>();

		setBackground(Color.BLACK);
		setLayout(new BorderLayout());

		loadGearImages();

		titleLabel = new JLabel();
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(titleLabel, BorderLayout.NORTH);

		thumbnailPanel = new JPanel();
		thumbnailPanel.setLayout(new BoxLayout(thumbnailPanel, BoxLayout.Y_AXIS));
		thumbnailPanel.setBackground(new Color(40, 40, 50));

		JScrollPane thumbnailScroll = new JScrollPane(thumbnailPanel);
		thumbnailScroll.setPreferredSize(new Dimension(320, 0));
		thumbnailScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		thumbnailScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.GRAY));

		gearCanvas = new GearCanvas();
		gearCanvas.setPreferredSize(new Dimension(viewportWidth, viewportHeight));
		gearCanvas.setFocusable(true);

		JScrollPane mainScroll = new JScrollPane(gearCanvas);
		mainScroll.setBorder(BorderFactory.createEmptyBorder());

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, thumbnailScroll, mainScroll);
		splitPane.setDividerLocation(320);
		splitPane.setResizeWeight(0);
		splitPane.setBackground(Color.BLACK);
		add(splitPane, BorderLayout.CENTER);

		Timer animationTimer = new Timer(16, e -> {
			activeAnimations.values().removeIf(GearAnimation::isFinished);
			for (Gear gear : gears) {
				gear.update();
			}
			gearCanvas.repaint();
		});
		animationTimer.start();

		lm.addLocaleChangeListener(this::updateTexts);
		updateTexts();

		highlightTimer = new Timer(500, e -> {
			highlightedGearId = null;
			gearCanvas.repaint();
		});
		highlightTimer.setRepeats(false);
	}

	private void loadGearImages() {
		String[] gearPaths = {"images/gear4.png", "images/gear5.png", "images/gear6.png"};
		for (String path : gearPaths) {
			URL imgUrl = getClass().getClassLoader().getResource(path);
			if (imgUrl != null) {
				ImageIcon icon = new ImageIcon(imgUrl);
				Image img = icon.getImage();
				gearImages.add(img);
			}
		}

		while (gearImages.size() < 3) {
			BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = img.createGraphics();
			g2d.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
			g2d.fillOval(0, 0, 100, 100);
			g2d.setColor(Color.BLACK);
			g2d.drawOval(0, 0, 100, 100);
			g2d.dispose();
			gearImages.add(img);
		}
	}

	private int getRandomGearSize() {
		int minGearSize = 40;
		int maxGearSize = 70;
		return minGearSize + random.nextInt(maxGearSize - minGearSize + 1);
	}

	private float getRandomSpeed() {
		return 1f + random.nextFloat() * 3f;
	}

	private int worldToScreenX(double worldX) {
		return (int) ((worldX - offsetX) * zoom + (double) viewportWidth / 2);
	}

	private int worldToScreenY(double worldY) {
		return (int) ((worldY - offsetY) * zoom + (double) viewportHeight / 2);
	}

	private void updateGearPositions() {
		for (Gear gear : gears) {
			if (gear.getWorker() != null && gear.getWorker().getCoordinates() != null) {
				gear.x = worldToScreenX(gear.getWorker().getCoordinates().getX());
				gear.y = worldToScreenY(gear.getWorker().getCoordinates().getY());
			}
		}
	}

	public void loadRealWorkers(List<Worker> realWorkers) {
		if (realWorkers == null || realWorkers.isEmpty()) {
			gears.clear();
			thumbnailPanel.removeAll();
			gearCanvas.repaint();
			return;
		}

		this.workers = realWorkers;
		worldMinX = Double.MAX_VALUE;
		worldMaxX = Double.MIN_VALUE;
		worldMinY = Double.MAX_VALUE;
		worldMaxY = Double.MIN_VALUE;
		hasCoordinates = false;
		for (Worker w : workers) {
			if (w.getCoordinates() != null) {
				hasCoordinates = true;
				long x = w.getCoordinates().getX();
				float y = w.getCoordinates().getY();
				if (x < worldMinX)
					worldMinX = x;
				if (x > worldMaxX)
					worldMaxX = x;
				if (y < worldMinY)
					worldMinY = y;
				if (y > worldMaxY)
					worldMaxY = y;
			}
		}

		if (hasCoordinates) {
			double paddingX = (worldMaxX - worldMinX) * 0.1;
			double paddingY = (worldMaxY - worldMinY) * 0.1;
			if (paddingX == 0)
				paddingX = 100;
			if (paddingY == 0)
				paddingY = 100;
			worldMinX -= paddingX;
			worldMaxX += paddingX;
			worldMinY -= paddingY;
			worldMaxY += paddingY;
			offsetX = (worldMinX + worldMaxX) / 2;
			offsetY = (worldMinY + worldMaxY) / 2;
			double rangeX = worldMaxX - worldMinX;
			double rangeY = worldMaxY - worldMinY;
			if (rangeX > 0 && rangeY > 0) {
				double zoomX = viewportWidth / rangeX;
				double zoomY = viewportHeight / rangeY;
				zoom = Math.min(zoomX, zoomY) * 0.9;
				zoom = Math.clamp(zoom, minZoom, maxZoom);
			}
		} else {
			worldMinX = 0;
			worldMaxX = 1000;
			worldMinY = 0;
			worldMaxY = 1000;
			offsetX = 500;
			offsetY = 500;
			zoom = 1.0;
		}
		updateWorkers(this.workers);
	}

	public void addWorker(Worker worker) {
		if (worker == null)
			return;
		if (worker.getCoordinates() != null) {
			GearAnimation animation = getGearAnimation(worker);
			activeAnimations.put(worker.getUuid(), animation);

			Timer addTimer = new Timer(500, evt -> {
				List<Worker> newList = new ArrayList<>(workers);
				newList.add(worker);
				workers = newList;
				updateWorkers(workers);
				activeAnimations.remove(worker.getUuid());
			});
			addTimer.setRepeats(false);
			addTimer.start();
		} else {
			List<Worker> newList = new ArrayList<>(workers);
			newList.add(worker);
			loadRealWorkers(newList);
		}
	}

	@Nonnull
	private GearAnimation getGearAnimation(Worker worker) {
		int targetSize = getRandomGearSize();
		float speed = getRandomSpeed();
		int imageIndex = Math.abs(worker.getUuid().hashCode()) % gearImages.size();
		Image originalImage = gearImages.get(imageIndex);
		Image scaledImage = originalImage.getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH);

		int screenX = worldToScreenX(worker.getCoordinates().getX());
		int screenY = worldToScreenY(worker.getCoordinates().getY());
		return new GearAnimation(GearAnimation.Type.GROW, screenX, screenY, 0, targetSize, speed, scaledImage);
	}

	public void updateWorker(Worker worker) {
		if (worker == null)
			return;
		Gear oldGear = findGearById(worker.getUuid());
		if (oldGear != null && worker.getCoordinates() != null) {
			int oldX = oldGear.x;
			int oldY = oldGear.y;
			int newX = worldToScreenX(worker.getCoordinates().getX());
			int newY = worldToScreenY(worker.getCoordinates().getY());
			int size = oldGear.getSize();
			float speed = getRandomSpeed();
			Image image = oldGear.getImage();
			gears.remove(oldGear);
			GearAnimation animation = new GearAnimation(GearAnimation.Type.MOVE, oldX, oldY, newX, newY, size, speed,
					image);
			activeAnimations.put(worker.getUuid(), animation);
			for (int i = 0; i < workers.size(); i++) {
				if (workers.get(i).getUuid().equals(worker.getUuid())) {
					workers.set(i, worker);
					break;
				}
			}
			Timer updateTimer = new Timer(500, evt -> {
				updateWorkers(workers);
				activeAnimations.remove(worker.getUuid());
			});
			updateTimer.setRepeats(false);
			updateTimer.start();
		} else {
			for (int i = 0; i < workers.size(); i++) {
				if (workers.get(i).getUuid().equals(worker.getUuid())) {
					workers.set(i, worker);
					break;
				}
			}
			loadRealWorkers(workers);
		}
	}

	public void removeWorker(String uuid) {
		Gear gearToRemove = findGearById(uuid);
		if (gearToRemove == null)
			return;

		int x = gearToRemove.x;
		int y = gearToRemove.y;
		int gearSize = gearToRemove.getSize();
		float speed = getRandomSpeed();
		Image image = gearToRemove.getImage();

		gears.remove(gearToRemove);
		GearAnimation animation = new GearAnimation(GearAnimation.Type.SHRINK, x, y, gearSize, 0, speed, image);
		activeAnimations.put(uuid, animation);

		Timer removeTimer = new Timer(500, evt -> {
			List<Worker> newList = new ArrayList<>();
			for (Worker w : workers) {
				if (!w.getUuid().equals(uuid)) {
					newList.add(w);
				}
			}
			loadRealWorkers(newList);
			activeAnimations.remove(uuid);
		});
		removeTimer.setRepeats(false);
		removeTimer.start();
	}

	private void updateWorkers(List<Worker> workers) {
		gears.clear();
		thumbnailPanel.removeAll();

		if (workers.isEmpty()) {
			JLabel emptyLabel = new JLabel(lm.getString("message.no_workers"));
			emptyLabel.setForeground(Color.WHITE);
			emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			thumbnailPanel.add(emptyLabel);
			thumbnailPanel.revalidate();
			thumbnailPanel.repaint();
			gearCanvas.repaint();
			return;
		}

		for (Worker worker : workers) {
			int gearSize = getRandomGearSize();
			int imageIndex = Math.abs(worker.getUuid().hashCode()) % gearImages.size();
			Image originalImage = gearImages.get(imageIndex);
			Image scaledImage = originalImage.getScaledInstance(gearSize, gearSize, Image.SCALE_SMOOTH);

			int screenX = viewportWidth / 2;
			int screenY = viewportHeight / 2;

			if (worker.getCoordinates() != null) {
				screenX = worldToScreenX(worker.getCoordinates().getX());
				screenY = worldToScreenY(worker.getCoordinates().getY());
			}

			Gear gear = new Gear(screenX, screenY, gearSize, getRandomSpeed(), scaledImage, worker);
			gears.add(gear);

			addThumbnailItem(worker, gearSize, scaledImage);
		}

		thumbnailPanel.revalidate();
		thumbnailPanel.repaint();
		gearCanvas.repaint();
	}

	private void addThumbnailItem(Worker worker, int gearSize, Image gearImage) {
		JPanel itemPanel = new JPanel();
		itemPanel.setLayout(new BorderLayout(10, 5));
		itemPanel.setBackground(new Color(60, 60, 80));
		itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		itemPanel.setMaximumSize(new Dimension(320, 85));
		itemPanel.setMinimumSize(new Dimension(320, 85));
		itemPanel.setPreferredSize(new Dimension(320, 85));
		itemPanel
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(80, 80, 100), 1),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		int thumbSize = Math.min(35, gearSize > 0 ? gearSize : 40);
		Image smallGear = gearImage != null
				? gearImage.getScaledInstance(thumbSize, thumbSize, Image.SCALE_SMOOTH)
				: null;
		JLabel gearLabel = new JLabel(smallGear != null ? new ImageIcon(smallGear) : new ImageIcon());

		String fullId = worker.getUuid();
		String shortId = fullId.length() > 12 ? fullId.substring(0, 10) + "..." : fullId;
		JLabel idLabel = new JLabel(shortId);
		idLabel.setForeground(Color.CYAN);
		idLabel.setFont(new Font("Monospaced", Font.BOLD, 11));

		String shortName = worker.getName().length() > 18
				? worker.getName().substring(0, 15) + "..."
				: worker.getName();
		JLabel nameLabel = new JLabel(shortName);
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

		String coordsText = "X:" + (worker.getCoordinates() != null ? worker.getCoordinates().getX() : "?") + " Y:"
				+ (worker.getCoordinates() != null ? worker.getCoordinates().getY() : "?");
		JLabel coordsLabel = new JLabel(coordsText);
		coordsLabel.setForeground(new Color(150, 200, 150));
		coordsLabel.setFont(new Font("Monospaced", Font.PLAIN, 10));

		JLabel sizeLabel = new JLabel(lm.getString("col.size") + ": " + gearSize + "px");
		sizeLabel.setForeground(new Color(255, 200, 100));
		sizeLabel.setFont(new Font("Arial", Font.PLAIN, 10));

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.setOpaque(false);
		textPanel.add(idLabel);
		textPanel.add(Box.createVerticalStrut(2));
		textPanel.add(nameLabel);
		textPanel.add(Box.createVerticalStrut(2));
		textPanel.add(coordsLabel);
		textPanel.add(Box.createVerticalStrut(2));
		textPanel.add(sizeLabel);

		itemPanel.add(gearLabel, BorderLayout.WEST);
		itemPanel.add(textPanel, BorderLayout.CENTER);

		itemPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (worker.getCoordinates() != null) {
						centerOnWorld(worker.getCoordinates().getX(), worker.getCoordinates().getY(), worker.getUuid());
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					showThumbnailContextMenu(worker, e.getX(), e.getY(), itemPanel);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				itemPanel.setBackground(new Color(90, 90, 110));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				itemPanel.setBackground(new Color(60, 60, 80));
			}
		});

		thumbnailPanel.add(itemPanel);
		thumbnailPanel.add(Box.createVerticalStrut(2));
	}

	private void showThumbnailContextMenu(Worker worker, int x, int y, JPanel itemPanel) {
		JPopupMenu menu = new JPopupMenu();

		JMenuItem editItem = new JMenuItem(lm.getString("command.update"));
		editItem.addActionListener(e -> {
			JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(AnimationPanel.this);
			Worker fullWorker = fetchFullWorkerData(worker.getUuid());
			if (fullWorker == null)
				fullWorker = worker;
			Object[] rowData = workerToRowData(fullWorker);
			WorkerFormDialog dialog = new WorkerFormDialog(frame, parent, currentUser, true, fullWorker.getUuid(),
					rowData);
			dialog.setVisible(true);
			SwingUtilities.invokeLater(this::refreshWorkersData);
		});

		JMenuItem deleteItem = new JMenuItem(lm.getString("command.remove"));
		deleteItem.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(AnimationPanel.this,
					lm.getString("confirm.delete_worker") + " " + worker.getName() + "?", lm.getString("confirm.title"),
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				edu.itmo.piikt.client.manager.GuiCommandSender.INSTANCE
						.sendCommand(edu.itmo.piikt.common.sc.ClientCommand.builder().nameCommand("remove_by_id")
								.user(currentUser).argumentCommand(worker.getUuid()).build());
				refreshWorkersData();
			}
		});

		JMenuItem infoItem = new JMenuItem(lm.getString("window.info"));
		infoItem.addActionListener(e -> showWorkerInfoDialog(worker));

		menu.add(editItem);
		menu.add(deleteItem);
		menu.addSeparator();
		menu.add(infoItem);

		menu.show(itemPanel, x, y);
	}

	private Worker fetchFullWorkerData(String workerId) {
		try {
			ClientCommand command = ClientCommand.builder().nameCommand("show").user(currentUser).build();

			ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

			if (response != null && response.execution() && response.data() != null) {
				List<String> workersList = response.data();
				for (String workerStr : workersList) {
					if (workerStr.contains("id: " + workerId) || workerStr.contains("id:" + workerId)) {
						return parseWorkerFromString(workerStr);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Worker parseWorkerFromString(String workerStr) {
		try {
			Worker worker = new Worker();
			worker.setUuid(extractValue(workerStr, "id:"));
			worker.setName(extractValue(workerStr, "name:"));
			String x = extractValue(workerStr, "х:");
			if (x == null)
				x = extractValue(workerStr, "x:");
			String y = extractValue(workerStr, "у:");
			if (y == null)
				y = extractValue(workerStr, "y:");
			if (x != null && y != null) {
				Coordinates coords = new Coordinates();
				coords.setX(Long.parseLong(x));
				coords.setY(Float.parseFloat(y));
				worker.setCoordinates(coords);
			}
			String salaryStr = extractValue(workerStr, "salary:");
			if (salaryStr != null && !salaryStr.isEmpty()) {
				worker.setSalary(Float.parseFloat(salaryStr));
			}

			String startDateStr = extractValue(workerStr, "startDate:");
			if (startDateStr != null && !startDateStr.isEmpty() && !"null".equals(startDateStr)) {
				try {
					if (startDateStr.startsWith("[") && startDateStr.endsWith("]")) {
						String[] parts = startDateStr.substring(1, startDateStr.length() - 1).split(",");
						if (parts.length >= 3) {
							int year = Integer.parseInt(parts[0].trim());
							int month = Integer.parseInt(parts[1].trim());
							int day = Integer.parseInt(parts[2].trim());
							worker.setStartDate(LocalDate.of(year, month, day));
						}
					} else {
						worker.setStartDate(LocalDate.parse(startDateStr));
					}
				} catch (Exception ignored) {
				}
			}

			String endDateStr = extractValue(workerStr, "endDate:");
			if (endDateStr != null && !endDateStr.isEmpty() && !"null".equals(endDateStr)) {
				try {
					if (endDateStr.startsWith("[") && endDateStr.endsWith("]")) {
						String[] parts = endDateStr.substring(1, endDateStr.length() - 1).split(",");
						if (parts.length >= 3) {
							int year = Integer.parseInt(parts[0].trim());
							int month = Integer.parseInt(parts[1].trim());
							int day = Integer.parseInt(parts[2].trim());
							worker.setEndDate(LocalDate.of(year, month, day));
						}
					} else {
						worker.setEndDate(LocalDate.parse(endDateStr));
					}
				} catch (Exception ignored) {
				}
			}

			String statusStr = extractValue(workerStr, "status:");
			if (statusStr != null && !statusStr.isEmpty()) {
				try {
					worker.setStatus(Status.valueOf(statusStr));
				} catch (IllegalArgumentException ignored) {
				}
			}

			Organization org = new Organization();
			boolean hasOrg = false;

			String turnoverStr = extractValue(workerStr, "annualTurnover:");
			if (turnoverStr != null && !turnoverStr.isEmpty()) {
				org.setAnnualTurnover(Integer.parseInt(turnoverStr));
				hasOrg = true;
			}

			String typeStr = extractValue(workerStr, "type:");
			if (typeStr != null && !typeStr.isEmpty()) {
				try {
					org.setType(OrganizationType.valueOf(typeStr));
					hasOrg = true;
				} catch (IllegalArgumentException ignored) {
				}
			}

			String streetStr = extractValue(workerStr, "street:");
			if (streetStr != null && !streetStr.isEmpty()) {
				Address address = new Address();
				address.setStreet(streetStr);
				org.setOfficialAddress(address);
				hasOrg = true;
			}

			if (hasOrg) {
				worker.setOrganization(org);
			}
			return worker;
		} catch (Exception e) {
			return null;
		}
	}

	private String extractValue(String text, String key) {
		if (text == null || key == null)
			return null;
		int startIndex = text.indexOf(key);
		if (startIndex == -1)
			return null;
		startIndex += key.length();
		while (startIndex < text.length() && text.charAt(startIndex) == ' ')
			startIndex++;
		if (startIndex >= text.length())
			return null;
		int endIndex = startIndex;
		while (endIndex < text.length()) {
			char c = text.charAt(endIndex);
			if (c == ',' || (c == ' ' && endIndex + 1 < text.length()
					&& (text.charAt(endIndex + 1) == 's' || text.charAt(endIndex + 1) == 't'))) {
				break;
			}
			endIndex++;
		}
		String value = text.substring(startIndex, endIndex).trim();
		if (value.endsWith(","))
			value = value.substring(0, value.length() - 1);
		return value.isEmpty() ? null : value;
	}

	private void showWorkerInfoDialog(Worker worker) {
		Worker fullWorker = fetchFullWorkerData(worker.getUuid());
		if (fullWorker == null)
			fullWorker = worker;

		StringBuilder info = new StringBuilder();
		info.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
		info.append(String.format("              %s\n", fullWorker.getName()));
		info.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");

		info.append(" ID: ").append(fullWorker.getUuid()).append("\n");
		info.append(" Name: ").append(fullWorker.getName()).append("\n\n");

		if (fullWorker.getCoordinates() != null) {
			info.append(" Coordinates:\n");
			info.append("   • X: ").append(fullWorker.getCoordinates().getX()).append("\n");
			info.append("   • Y: ").append(fullWorker.getCoordinates().getY()).append("\n\n");
		}

		info.append(" Salary: ")
				.append(fullWorker.getSalary() != null ? String.format("%.2f", fullWorker.getSalary()) : "null")
				.append("\n\n");

		if (fullWorker.getStatus() != null) {
			info.append(" Status: ").append(fullWorker.getStatus()).append("\n");
		}

		if (fullWorker.getStartDate() != null) {
			info.append(" Start date: ").append(fullWorker.getStartDate()).append("\n");
		}

		if (fullWorker.getEndDate() != null) {
			info.append(" End date: ").append(fullWorker.getEndDate()).append("\n");
		}

		if (fullWorker.getOrganization() != null) {
			info.append("\n Organization:\n");
			info.append("   • Annual turnover: ").append(fullWorker.getOrganization().getAnnualTurnover()).append("\n");
			if (fullWorker.getOrganization().getType() != null) {
				info.append("   • Type: ").append(fullWorker.getOrganization().getType()).append("\n");
			}
			if (fullWorker.getOrganization().getOfficialAddress() != null) {
				info.append("   • Address: ").append(fullWorker.getOrganization().getOfficialAddress().getStreet())
						.append("\n");
			}
		}

		JOptionPane.showMessageDialog(AnimationPanel.this, info.toString(), "Worker Info - " + fullWorker.getName(),
				JOptionPane.INFORMATION_MESSAGE);
	}

	private Object[] workerToRowData(Worker worker) {
		Object[] row = new Object[11];
		row[0] = worker.getUuid();
		row[1] = worker.getName();
		row[2] = worker.getCoordinates() != null ? worker.getCoordinates().getX() : "";
		row[3] = worker.getCoordinates() != null ? worker.getCoordinates().getY() : "";
		row[4] = worker.getSalary();
		row[5] = worker.getStartDate() != null ? worker.getStartDate().toString() : "";
		row[6] = worker.getEndDate() != null ? worker.getEndDate().toString() : "";
		row[7] = worker.getStatus() != null ? worker.getStatus().toString() : "";

		if (worker.getOrganization() != null) {
			row[8] = worker.getOrganization().getAnnualTurnover();
			row[9] = worker.getOrganization().getType() != null ? worker.getOrganization().getType().toString() : "";
			row[10] = worker.getOrganization().getOfficialAddress() != null
					? worker.getOrganization().getOfficialAddress().getStreet()
					: "";
		} else {
			row[8] = "";
			row[9] = "";
			row[10] = "";
		}
		return row;
	}

	private void centerOnWorld(double worldX, double worldY, String gearId) {
		offsetX = worldX;
		offsetY = worldY;
		updateGearPositions();

		highlightedGearId = gearId;
		highlightTimer.restart();

		gearCanvas.repaint();
	}

	private Gear findGearById(String id) {
		for (Gear gear : gears) {
			if (gear.getWorker() != null && gear.getWorker().getUuid().equals(id)) {
				return gear;
			}
		}
		return null;
	}

	private void updateTexts() {
		titleLabel.setText(lm.getString("command.animation"));
	}

	private void refreshWorkersData() {
		new Thread(() -> {
			try {
				ClientCommand command = ClientCommand.builder().nameCommand("show").user(currentUser).build();

				ServerResponse response = GuiCommandSender.INSTANCE.sendCommand(command);

				if (response != null && response.execution() && response.data() != null) {
					List<String> workersList = response.data();
					List<Worker> updatedWorkers = new ArrayList<>();
					for (String workerStr : workersList) {
						Worker w = parseWorkerFromString(workerStr);
						if (w != null) {
							updatedWorkers.add(w);
						}
					}
					SwingUtilities.invokeLater(() -> loadRealWorkers(updatedWorkers));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	private static class Gear {
		private int x, y;
		@Getter
		private final int size;
		private final float speed;
		private float angle;
		@Getter
		private final Image image;
		@Getter
		private final Worker worker;

		public Gear(int x, int y, int size, float speed, Image image, Worker worker) {
			this.x = x;
			this.y = y;
			this.size = size;
			this.speed = speed;
			this.angle = 0;
			this.image = image;
			this.worker = worker;
		}

		public void update() {
			if (speed > 0) {
				angle += speed;
				if (angle > 360)
					angle -= 360;
			}
		}

		public void draw(Graphics2D g2d) {
			if (image == null)
				return;
			if (speed > 0) {
				g2d.rotate(Math.toRadians(angle), x + size / 2.0, y + size / 2.0);
			}
			g2d.drawImage(image, x, y, size, size, null);
			if (speed > 0) {
				g2d.rotate(-Math.toRadians(angle), x + size / 2.0, y + size / 2.0);
			}
		}

		public boolean contains(int mouseX, int mouseY) {
			return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
		}

		public String getWorkerId() {
			return worker.getUuid();
		}
	}

	private static class GearAnimation {

		enum Type {
			GROW, SHRINK, MOVE
		}

		private final Type type;
		private final int startX;
		private final int startY;
		private final int targetX;
		private final int targetY;
		private final int startSize;
		private final int targetSize;
		@Getter
		private final float speed;
		private final Image image;
		private final long startTime;
		private static final long DURATION = 500;

		public GearAnimation(Type type, int x, int y, int startSize, int targetSize, float speed, Image image) {
			this.type = type;
			this.startX = x;
			this.startY = y;
			this.targetX = x;
			this.targetY = y;
			this.startSize = startSize;
			this.targetSize = targetSize;
			this.speed = speed;
			this.image = image;
			this.startTime = System.currentTimeMillis();
		}

		public GearAnimation(Type type, int oldX, int oldY, int newX, int newY, int size, float speed, Image image) {
			this.type = type;
			this.startX = oldX;
			this.startY = oldY;
			this.targetX = newX;
			this.targetY = newY;
			this.startSize = size;
			this.targetSize = size;
			this.speed = speed;
			this.image = image;
			this.startTime = System.currentTimeMillis();
		}

		public boolean isFinished() {
			return System.currentTimeMillis() - startTime >= DURATION;
		}

		public void draw(Graphics2D g2d) {
			long elapsed = System.currentTimeMillis() - startTime;
			float progress = Math.min(1f, elapsed / (float) DURATION);

			int currentSize;
			int currentX, currentY;
			float currentAngle = progress * 360 * 2;

			switch (type) {
				case GROW, SHRINK :
					currentSize = (int) (startSize + (targetSize - startSize) * progress);
					currentX = startX;
					currentY = startY;
					break;
				case MOVE :
					currentSize = startSize;
					currentX = (int) (startX + (targetX - startX) * progress);
					currentY = (int) (startY + (targetY - startY) * progress);
					break;
				default :
					currentSize = startSize;
					currentX = startX;
					currentY = startY;
			}

			if (image != null && currentSize > 0) {
				g2d.rotate(Math.toRadians(currentAngle), currentX + currentSize / 2.0, currentY + currentSize / 2.0);
				g2d.drawImage(image, currentX, currentY, currentSize, currentSize, null);
				g2d.rotate(-Math.toRadians(currentAngle), currentX + currentSize / 2.0, currentY + currentSize / 2.0);
			}
		}
	}

	private class GearCanvas extends JPanel {
		public GearCanvas() {
			setBackground(Color.BLACK);
			setFocusable(true);
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					double step = 50 / zoom;
					switch (e.getKeyCode()) {
						case KeyEvent.VK_LEFT :
						case KeyEvent.VK_A :
							offsetX -= step;
							break;
						case KeyEvent.VK_RIGHT :
						case KeyEvent.VK_D :
							offsetX += step;
							break;
						case KeyEvent.VK_UP :
						case KeyEvent.VK_W :
							offsetY -= step;
							break;
						case KeyEvent.VK_DOWN :
						case KeyEvent.VK_S :
							offsetY += step;
							break;
						case KeyEvent.VK_PLUS :
						case KeyEvent.VK_EQUALS :
							zoom = Math.min(maxZoom, zoom * 1.2);
							break;
						case KeyEvent.VK_MINUS :
							zoom = Math.max(minZoom, zoom / 1.2);
							break;
						case KeyEvent.VK_R :
							if (hasCoordinates) {
								offsetX = (worldMinX + worldMaxX) / 2;
								offsetY = (worldMinY + worldMaxY) / 2;
							} else {
								offsetX = 500;
								offsetY = 500;
							}
							zoom = 1.0;
							break;
					}
					updateGearPositions();
					repaint();
				}
			});

			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown()) {
						dragging = true;
						dragStartX = e.getX();
						dragStartY = e.getY();
						dragStartOffsetX = offsetX;
						dragStartOffsetY = offsetY;
						setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					} else if (e.getButton() == MouseEvent.BUTTON1) {
						for (Gear gear : gears) {
							if (gear.contains(e.getX(), e.getY()) && gear.getWorker() != null) {
								showWorkerInfoDialog(gear.getWorker());
								break;
							}
						}
					} else if (e.getButton() == MouseEvent.BUTTON3) {
						for (Gear gear : gears) {
							if (gear.contains(e.getX(), e.getY()) && gear.getWorker() != null) {
								showGearContextMenu(gear.getWorker(), e.getX(), e.getY());
								break;
							}
						}
					}
					requestFocusInWindow();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (dragging) {
						dragging = false;
						setCursor(Cursor.getDefaultCursor());
					}
				}
			});

			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					if (dragging) {
						offsetX = dragStartOffsetX - (e.getX() - dragStartX) / zoom;
						offsetY = dragStartOffsetY - (e.getY() - dragStartY) / zoom;
						updateGearPositions();
						repaint();
					}
				}
			});

			addMouseWheelListener(e -> {
				double scale = e.getWheelRotation() < 0 ? 1.1 : 0.9;
				zoom = Math.clamp(zoom * scale, minZoom, maxZoom);
				updateGearPositions();
				repaint();
			});

			addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					viewportWidth = getWidth();
					viewportHeight = getHeight();
					updateGearPositions();
					repaint();
				}
			});
		}

		private void showGearContextMenu(Worker worker, int x, int y) {
			JPopupMenu menu = new JPopupMenu();
			JMenuItem editItem = new JMenuItem(lm.getString("command.update"));
			editItem.addActionListener(e -> {
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(AnimationPanel.this);
				Worker fullWorker = fetchFullWorkerData(worker.getUuid());
				if (fullWorker == null)
					fullWorker = worker;
				Object[] rowData = workerToRowData(fullWorker);
				WorkerFormDialog dialog = new WorkerFormDialog(frame, parent, currentUser, true, fullWorker.getUuid(),
						rowData);
				dialog.setVisible(true);

				SwingUtilities.invokeLater(AnimationPanel.this::refreshWorkersData);
			});

			JMenuItem deleteItem = new JMenuItem(lm.getString("command.remove"));
			deleteItem.addActionListener(e -> {
				int confirm = JOptionPane.showConfirmDialog(AnimationPanel.this,
						lm.getString("confirm.delete_worker") + " " + worker.getName() + "?",
						lm.getString("confirm.title"), JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					edu.itmo.piikt.client.manager.GuiCommandSender.INSTANCE
							.sendCommand(edu.itmo.piikt.common.sc.ClientCommand.builder().nameCommand("remove_by_id")
									.user(currentUser).argumentCommand(worker.getUuid()).build());
					refreshWorkersData();
				}
			});

			JMenuItem infoItem = new JMenuItem(lm.getString("window.info"));
			infoItem.addActionListener(e -> {
				showWorkerInfoDialog(worker);
			});

			menu.add(editItem);
			menu.add(deleteItem);
			menu.addSeparator();
			menu.add(infoItem);
			menu.show(AnimationPanel.this, x, y);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setColor(new Color(20, 20, 30));
			g2d.fillRect(0, 0, getWidth(), getHeight());
			g2d.setColor(new Color(60, 60, 80));
			g2d.setStroke(new BasicStroke(1));
			double gridStep = Math.pow(10, Math.floor(Math.log10(100 / zoom)));
			double startGridX = Math.floor(offsetX / gridStep) * gridStep - gridStep * 5;
			double startGridY = Math.floor(offsetY / gridStep) * gridStep - gridStep * 5;
			for (double x = startGridX; x < offsetX + gridStep * 10; x += gridStep) {
				int screenX = worldToScreenX(x);
				if (screenX >= 0 && screenX <= getWidth()) {
					g2d.drawLine(screenX, 0, screenX, getHeight());
					g2d.drawString(String.format("%.0f", x), screenX + 2, 15);
				}
			}

			for (double y = startGridY; y < offsetY + gridStep * 10; y += gridStep) {
				int screenY = worldToScreenY(y);
				if (screenY >= 0 && screenY <= getHeight()) {
					g2d.drawLine(0, screenY, getWidth(), screenY);
					g2d.drawString(String.format("%.0f", y), 5, screenY - 2);
				}
			}
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
			g2d.drawString(String.format("%s: %.2fx | %s: (%.0f, %.0f)", lm.getString("animation.zoom"), zoom,
					lm.getString("animation.position"), offsetX, offsetY), 10, 30);
			g2d.drawString(lm.getString("animation.controls"), 10, 55);

			for (GearAnimation anim : activeAnimations.values()) {
				anim.draw(g2d);
			}
			for (Gear gear : gears) {
				gear.draw(g2d);
			}
			if (highlightedGearId != null) {
				for (Gear gear : gears) {
					if (gear.getWorkerId() != null && gear.getWorkerId().equals(highlightedGearId)) {
						g2d.setColor(new Color(255, 255, 0, 200));
						g2d.setStroke(new BasicStroke(4));
						g2d.drawRect(gear.x - 3, gear.y - 3, gear.size + 6, gear.size + 6);
						break;
					}
				}
			}
		}
	}
}
