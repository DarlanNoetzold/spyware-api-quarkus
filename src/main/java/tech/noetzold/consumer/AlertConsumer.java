package tech.noetzold.consumer;


import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.noetzold.controller.AlertController;
import tech.noetzold.model.Alert;
import tech.noetzold.service.AlertService;

import java.util.Calendar;

public class AlertConsumer {

    @Inject
    AlertService alertService;


    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    @Incoming("alerts")
    @Blocking
    public Alert process(Alert incomingAlert) {
        incomingAlert.setDataCadastro(Calendar.getInstance());
        alertService.saveAlert(incomingAlert);
        logger.info("Create Alert " + incomingAlert.getId() + " for user " + incomingAlert.getPcId() + " generate by " + incomingAlert.getImage().getProductImg());

        return incomingAlert;
    }
}
