package de.bst.reactive.infrastructure;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;

import com.melowe.jms2.compat.Jms2ConnectionFactory;
import com.mockrunner.mock.jms.JMSMockObjectFactory;
import com.mockrunner.mock.jms.MockQueue;

import de.bst.reactive.ReactiveConsumer;

public class TestConfiguration {

	@Produces
	@ApplicationScoped
	public MockQueue inQueue(ConnectionFactory connectionFactory) {
		return (MockQueue) connectionFactory.createContext().createQueue(ReactiveConsumer.QUEUE_NAME);
	}

	@Produces
	@ApplicationScoped
	public ConnectionFactory connectionFactory() {
		return new Jms2ConnectionFactory(new JMSMockObjectFactory().createMockConnectionFactory());
	}

	@Produces
	@ApplicationScoped
	public JMSContext jmsContext(ConnectionFactory factory) {
		return factory.createContext();
	}
}
