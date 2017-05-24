package de.bst.infrastructure;

import javax.enterprise.inject.spi.CDI;

import org.jboss.weld.environment.se.Weld;

public class WeldContext {

	public static final WeldContext INSTANCE = new WeldContext();

	private WeldContext() {
		final Weld weld = new Weld();
		weld.initialize();
	}

	public <T> T getBean(Class<T> type) {
		return CDI.current().select(type).get();
	}
}