package de.inmysparetime.vodim;

import java.util.Optional;
import java.util.function.UnaryOperator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.inmysparetime.vodim.reader.MetricsReader;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class ServiceFactory {
	private final static Logger LOG = LogManager.getLogger(ServiceFactory.class);

	public Service<Optional<Metrics>> createInitializationService(final Processor processor, final UnaryOperator<Metrics> onSuccess) {

		Service<Optional<Metrics>> service = new Service<Optional<Metrics>>() {

			@Override
			protected Task<Optional<Metrics>> createTask() {
				Task<Optional<Metrics>> firstReadOfMetrix = new Task<Optional<Metrics>>() {

					@Override
					protected Optional<Metrics> call() throws Exception {
						return processor.initialize();
					}
				};
				return firstReadOfMetrix;
			}
		};
		service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				// TODO: Hier muss die erste Metrics ausgewertet werden:
				// For each type (gauge, counter, meter, histogram, ...) there will
				// be a panel.
				final Optional<Metrics> result = service.getValue();
				if (result.isPresent()) {
					onSuccess.apply(result.get());
				}
			}
		});

		
		return service;
	}
	public ScheduledService<Metrics> createUpdateService(final Duration period, final MetricsReader reader, final MetricsHistory history) {

		ScheduledService<Metrics> service = new ScheduledService<Metrics>() {

			@Override
			protected Task<Metrics> createTask() {
				Task<Metrics> firstReadOfMetrix = new Task<Metrics>() {
					@Override
					protected Metrics call() throws Exception {
						LOG.trace("Calling cyclic update.");
						return reader.readMetrics();
					}
				};
				return firstReadOfMetrix;
			}
		};
		service.setOnSucceeded((WorkerStateEvent event) -> {
			history.update(service.getValue());			
		});
		service.setPeriod(period);
		return service;
	}
}
