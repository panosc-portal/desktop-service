package eu.panosc.portal.business;

import com.google.inject.Inject;
import eu.panosc.portal.core.domain.InstanceSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(BusinessExtension.class)
public class InstanceSessionServiceTest {

    @Inject
        private InstanceSessionService instanceSessionService;

    @Test
    @DisplayName("Get an instance session by a known id")
    void testGetById() {
        Long id = 1000L;
        InstanceSession instanceSession = instanceSessionService.getById(id);
        assertNotNull(instanceSession);
        assertEquals(id, instanceSession.getId());
    }

    @Test
    @DisplayName("Get an instance session by a known instance")
    void testGetByInstance() {
        InstanceSession instanceSession = instanceSessionService.getByInstanceId(1000L);
        assertNotNull(instanceSession);
        assertEquals(5, instanceSession.getClientCount());
    }

    @Test
    @DisplayName("Get an instance session by an unknown id")
    void testGetByUnknownId() {
        InstanceSession instanceSession = instanceSessionService.getById(1000000L);
        assertNull(instanceSession);
    }

    @Test
    @DisplayName("Get all instance sessions")
    void testGetAll() {
        List<InstanceSession> results = instanceSessionService.getAll();
        assertEquals(3, results.size());
    }

    @Test
    @DisplayName("Delete an instance session")
    void testDelete() {
        InstanceSession instanceSession = instanceSessionService.getById(1002L);
        instanceSessionService.delete(instanceSession);
        List<InstanceSession> results = instanceSessionService.getAll();
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Create a new instance session")
    void create() {
        InstanceSession session = new InstanceSession(1000L, "a-connection-id");
        instanceSessionService.save(session);
        InstanceSession persistedInstanceSession = instanceSessionService.getById(session.getId());
        assertNotNull(persistedInstanceSession);
        assertEquals("a-connection-id", persistedInstanceSession.getConnectionId());
    }

    @Test
    @DisplayName("Increment the client count for a given instance session")
    void incrementClientCountForSession() {
        InstanceSession instanceSession = instanceSessionService.getById(1000L);
        instanceSessionService.onClientAdded(instanceSession);
        InstanceSession persistedInstanceSession = instanceSessionService.getById(1000L);
        assertEquals(6, persistedInstanceSession.getClientCount());
    }

    @Test
    @DisplayName("Decrement the client count for a given instance session")
    void decrementClientCountForSession() {
        InstanceSession instanceSession = instanceSessionService.getById(1000L);
        instanceSessionService.onClientRemoved(instanceSession);
        InstanceSession persistedInstanceSession = instanceSessionService.getById(1000L);
        assertEquals(4, persistedInstanceSession.getClientCount());
    }

    @Test
    @DisplayName("Fail to decrement an instance session client count because client count is zero")
    void testFailToDecrementInstanceSessionClientCountBecauseClientCountIsZero() {
        InstanceSession instanceSession = instanceSessionService.getById(1002L);
        assertThrows(RuntimeException.class,
            () -> instanceSessionService.onClientRemoved(instanceSession),
            "Client count cannot be less than zero");
    }

    @Test
    @DisplayName("Test deleted when client count zero")
    void testDeletedWhenClientCountZero() {
        InstanceSession session = instanceSessionService.create(2000L, "a-connection-id");

        // Increase client count
        instanceSessionService.onClientAdded(session);

        InstanceSession persistedInstanceSession = instanceSessionService.getById(session.getId());
        assertNotNull(persistedInstanceSession);
        assertEquals(1, persistedInstanceSession.getClientCount());

        // Decrease client count
        instanceSessionService.onClientRemoved(session);
        InstanceSession deletedInstanceSession = instanceSessionService.getById(session.getId());
        assertNull(deletedInstanceSession);
        assertEquals(0, persistedInstanceSession.getClientCount());
    }


}
