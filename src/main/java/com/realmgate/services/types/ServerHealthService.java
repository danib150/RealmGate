package com.realmgate.services.types;

import com.realmgate.RealmGatePlugin;
import com.realmgate.server.BackendServer;
import com.realmgate.services.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerHealthService implements Service {

    private final Collection<BackendServer> servers;
    private final Duration timeout;
    private final long periodSeconds;

    private ScheduledExecutorService scheduler;

    public ServerHealthService(Collection<BackendServer> servers, Duration timeout, long periodSeconds) {
        this.servers = servers;
        this.timeout = timeout;
        this.periodSeconds = periodSeconds;
    }


    @Override
    public void init() {
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "RealmGate-HealthCheck");
            t.setDaemon(true);
            return t;
        });

        scheduler.scheduleAtFixedRate(this::checkAll, 0, periodSeconds, TimeUnit.SECONDS);

    }

    @Override
    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    private void checkAll() {
        for (BackendServer server : servers) {
            boolean old = server.isReachable();
            boolean reachable = PortChecker.udpResponds(server.getAddress(), server.getPort(), 300);

            server.setReachable(reachable);

            if (old != reachable) {
                RealmGatePlugin.getInstance().getLogger().atInfo().log("Server {} is now {}", server.getName(), reachable ? "reachable" : "unreachable");
            }

        }
    }

}
