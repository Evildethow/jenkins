package jenkins.slaves;

import hudson.Extension;
import hudson.model.AdministrativeMonitor;
import hudson.model.Node;
import hudson.model.Slave;
import hudson.remoting.Launcher;
import jenkins.model.Jenkins;

import javax.inject.Inject;
import java.io.IOException;

@Extension
public class SlaveVersionMonitor extends AdministrativeMonitor {

    private static final String UNKNOWN_SLAVE_VERSION = "< 1.335";

    @Inject
    Jenkins jenkins;

    @Override
    public boolean isActivated() {
        for(Node node : jenkins.getNodes()) {
            if (node instanceof Slave) {
                Slave slave = (Slave)node;
                String slaveVersion = getSlaveVersion(slave);
                if (!slaveVersion.equals(Launcher.VERSION) && !slaveVersion.equals(UNKNOWN_SLAVE_VERSION)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getSlaveVersion(Slave slave) {
        try {
            return slave.getComputer() != null ? slave.getComputer().getSlaveVersion() : UNKNOWN_SLAVE_VERSION;
        } catch (IOException | InterruptedException e) {
            return UNKNOWN_SLAVE_VERSION;
        }
    }
}