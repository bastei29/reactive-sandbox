package de.bst.reactive;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;

import reactor.core.publisher.Flux;

@ApplicationScoped
public class ReactiveConsumer {

	public static final String QUEUE_NAME = "InQueue";

	@Inject
	private Queue queue;

	@Inject
	private JMSContext jmsContext;

	public Flux<Message> messages() {
		return Flux.create(sink -> {
			try {
				jmsContext.createConsumer(queue).setMessageListener(message -> sink.next(message));

				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						sink.complete();
					}
				});

				sink.onRequest(l -> sink.next(jmsContext.createTextMessage("pre-first message")));

			} catch (final Exception e) {
				sink.error(e);
			}
		});
	}
}
