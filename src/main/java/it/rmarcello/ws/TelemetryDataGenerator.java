package it.rmarcello.ws;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TelemetryDataGenerator  implements Runnable {

    private static final Logger LOG = Logger.getLogger(TelemetryDataGenerator.class);

    @ConfigProperty(name = "datagenerator.delay")
    long delay;

    @Inject
    TelemetryWebsocket ws;

    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    void onStart(@Observes StartupEvent ev) {
        scheduler.scheduleWithFixedDelay(this, 0L, delay, TimeUnit.MILLISECONDS);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        LOG.info("run!");
        TelemetryData data = new TelemetryData();
        data.setType( random.nextInt(100) );
        data.setDescr("Description_" + data.getType() );
        data.setValue( random.nextDouble() );
        String msg = new Gson().toJson(data);
        ws.sendTelemetryData( msg );
    }
}
