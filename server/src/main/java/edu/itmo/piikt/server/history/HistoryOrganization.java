package edu.itmo.piikt.server.history;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Organization;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Getter
@NoArgsConstructor
public enum HistoryOrganization {
    INSTANCE;
    private final static AppLogger logger = new AppLogger(HistoryOrganization.class);
    private final List<Organization> list = new ArrayList<>();

    public void add(Organization organization) {
        try (Context ignored = Context.newId()) {
            logger.debug("Adding worker: type={}", organization.getType());
            boolean flag = true;
            for (Organization organization1 : list) {
                if(organization1.equals(organization)) {
                    flag = false;
                    break;
                }
            }
            if (flag) list.add(organization);
            logger.debug("Collection size: {}", list.size());
        } catch (Exception e) {
            logger.error("Error adding collection: {}", e);
            throw new RuntimeException(e);
        }
    }
}