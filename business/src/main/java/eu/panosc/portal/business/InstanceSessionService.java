package eu.panosc.portal.business;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import eu.panosc.portal.InstanceSessionRepository;
import eu.panosc.portal.core.domain.InstanceSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;

@Transactional
public class InstanceSessionService {

    private static final Logger logger = LoggerFactory.getLogger(InstanceSessionService.class);

    private InstanceSessionRepository repository;

    @Inject
    public InstanceSessionService(InstanceSessionRepository repository) {
        this.repository = repository;
    }

    public List<InstanceSession> getAll() {
        return this.repository.getAll();
    }

    public InstanceSession getById(@NotNull Long id) {
        return this.repository.getById(id);
    }

    public InstanceSession create(@NotNull Long instanceId, String connectionId) {
        InstanceSession session = new InstanceSession(instanceId, connectionId);

        this.save(session);

        return session;
    }

    public void updateLastSeenAt(InstanceSession instanceSession) {
        instanceSession.updateLastSeenAt();
        save(instanceSession);
    }

    public InstanceSession getByInstanceId(@NotNull Long instanceId) {
        return this.repository.getByInstanceId(instanceId);
    }

    public void delete(@NotNull InstanceSession InstanceSession) {
        this.repository.delete(InstanceSession);
    }

    public void save(@NotNull InstanceSession instanceSession) {
        this.repository.save(instanceSession);
    }

    public void onClientAdded(@NotNull InstanceSession instanceSession) {
        instanceSession.incrementClientCount();
        this.repository.save(instanceSession);
    }

    public void onClientRemoved(@NotNull InstanceSession instanceSession) {
        if (instanceSession.getClientCount() == 0) {
            throw new RuntimeException("Client count cannot be less than zero");
        }

        instanceSession.decrementClientCount();

        if (instanceSession.getClientCount() == 0) {
            logger.info("Deleting instance session with Id: {}", instanceSession.getId());
            this.repository.delete(instanceSession);

        } else {
            this.repository.save(instanceSession);
        }

    }

}
