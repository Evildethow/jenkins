package jenkins.slaves;

import hudson.model.Node;
import hudson.model.Slave;
import hudson.remoting.Launcher;
import hudson.slaves.SlaveComputer;
import jenkins.model.Jenkins;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SlaveVersionMonitorTest {

    @Mock
    private Jenkins jenkins;

    private SlaveVersionMonitor subject;

    @Before
    public void setUp() throws Exception {
        subject = new SlaveVersionMonitor();
        subject.jenkins = jenkins;
    }

    @Test
    public void testActive() throws Exception {
        List<Node> testSlaves = asNodeList("1.01", Launcher.VERSION);

        when(jenkins.getNodes()).thenReturn(testSlaves);

        assertThat(subject.isActivated(), is(true));
    }

    @Test
    public void inActive() throws Exception {
        List<Node> testSlaves = asNodeList(Launcher.VERSION, Launcher.VERSION);

        when(jenkins.getNodes()).thenReturn(testSlaves);

        assertThat(subject.isActivated(), is(false));
    }

    private static List<Node> asNodeList(String... versions) {
        List<Node> nodes = new ArrayList<>();

        for (String version : versions) {
            nodes.add(createSlave(version));
        }
        return nodes;
    }

    private static Slave createSlave(String slaveVersion) {
        Slave slave = mock(Slave.class);
        SlaveComputer slaveComputer = mock(SlaveComputer.class);

        when(slave.getComputer()).thenReturn(slaveComputer);

        try {
            when(slaveComputer.getSlaveVersion()).thenReturn(slaveVersion);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return slave;
    }
}
