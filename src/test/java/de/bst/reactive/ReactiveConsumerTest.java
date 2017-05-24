package de.bst.reactive;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mockrunner.mock.jms.MockQueue;
import com.mockrunner.mock.jms.MockTextMessage;

import de.bst.infrastructure.WeldJUnit4Runner;

@RunWith(WeldJUnit4Runner.class)
public class ReactiveConsumerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveConsumerTest.class);

	@Inject
	private MockQueue inQueue;

	@Inject
	private ReactiveConsumer reactiveConsumer;

	@Test
	public void test() throws JMSException {
		reactiveConsumer.messages().log().map(this::body).subscribe(LOGGER::info, error -> LOGGER.error(error.getMessage()));

		for (int i = 0; i < 10; i++) {
			inQueue.addMessage(generateMessage(i));
		}
	}

	private String body(Message message) {
		try {
			return message.getBody(String.class);
		} catch (final JMSException e) {
			throw new RuntimeException(e);
		}
	}

	private Message generateMessage(int index) throws JMSException {
		final MockTextMessage message = new MockTextMessage();
		message.setText(String.format("This is a number: %s", index));
		return message;
	}
}
