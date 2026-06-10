package edu.itmo.piikt.client.gui.localization;

import lombok.Getter;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.*;

public class LocaleManager {
	private static LocaleManager instance;
	private ResourceBundle bundle;
	@Getter
	private String currentLang;
	private final List<Runnable> listeners = new ArrayList<>();

	private LocaleManager() {
		currentLang = "ru";
		loadLocale();
	}

	public static LocaleManager getInstance() {
		if (instance == null) {
			instance = new LocaleManager();
		}
		return instance;
	}

	public void setLocale(String langCode) {
		if (langCode == null || langCode.isEmpty()) {
			return;
		}

		if (currentLang.equals(langCode)) {
			return;
		}

		this.currentLang = langCode;
		loadLocale();
		notifyListeners();
	}

	private void loadLocale() {
		ResourceBundle.clearCache();

		try {
			String countryCode = currentLang.toUpperCase(Locale.ROOT);
			Locale locale = new Locale(currentLang, countryCode);
			bundle = ResourceBundle.getBundle("i18n.messages", locale);
			return;
		} catch (MissingResourceException e) {
			// todo
		}

		try {
			Locale fallbackLocale = new Locale(currentLang);
			bundle = ResourceBundle.getBundle("i18n.messages", fallbackLocale);
			return;
		} catch (MissingResourceException e) {
			// todo
		}

		try {
			bundle = ResourceBundle.getBundle("i18n.messages", Locale.ROOT);
			return;
		} catch (MissingResourceException e) {
			// todo
		}

		bundle = new ResourceBundle() {
			@Override
			protected Object handleGetObject(@Nonnull String key) {
				return key;
			}

			@Override
			public Enumeration<String> getKeys() {
				return Collections.emptyEnumeration();
			}

			@Override
			public boolean containsKey(@Nonnull String key) {
				return true;
			}
		};
	}

	public String getString(String key) {
		if (key == null || key.isEmpty()) {
			return "";
		}

		try {
			if (bundle == null) {
				return key;
			}

			if (bundle.containsKey(key)) {
				String value = bundle.getString(key);
				if (value.isEmpty()) {
					return key;
				}
				return value;
			} else {
				return key;
			}
		} catch (Exception e) {
			return key;
		}
	}

	public void addLocaleChangeListener(Runnable listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public void removeLocaleChangeListener(Runnable listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

	private void notifyListeners() {
		if (listeners.isEmpty()) {
			return;
		}

		SwingUtilities.invokeLater(() -> {
			for (Runnable listener : listeners) {
				try {
					listener.run();
				} catch (Exception e) {
					// todo
				}
			}
		});
	}
}
