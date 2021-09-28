package it.rmarcello.ws;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.jboss.logging.Logger;

@ServerEndpoint("/telemetry")
@ApplicationScoped
public class TelemetryWebsocket {

    private static final Logger LOG = Logger.getLogger(TelemetryWebsocket.class);

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        LOG.info("onOpen: " + session.getId() );
        sessions.put(session.getId(), session);
    }

    @OnClose
    public void onClose(Session session) {
        LOG.info("onClose: " + session.getId() );
        sessions.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessions.remove(session.getId());
        LOG.error("onError", throwable);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        session.getAsyncRemote().sendObject("thanks");
    }

    public void sendTelemetryData(String msg) {
        

        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(msg, result -> {
                if (result.getException() != null) {
                    LOG.warn("error", result.getException());
                }
            });
        });


    }

}
