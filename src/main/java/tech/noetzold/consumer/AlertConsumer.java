package tech.noetzold.consumer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import tech.noetzold.model.Alert;
import tech.noetzold.service.AlertService;

public class AlertConsumer {

    @Inject
    AlertService alertService;

    @Incoming("alerts")
    @Outgoing("quotes")
    @Blocking
    public Alert process(Alert alert) throws InterruptedException {
        return alertService.saveAlerta(alert);

    }
}
