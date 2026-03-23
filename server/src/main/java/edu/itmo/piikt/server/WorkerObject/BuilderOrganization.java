package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.server.validation.modelValidation.ValidationAddress;
import edu.itmo.piikt.server.validation.modelValidation.ValidationOrganization;
import edu.itmo.piikt.server.validation.modelValidation.ValidationOrganizationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class BuilderOrganization implements Serializable {
    private ValidationOrganizationType validationOrganizationType;
    private ValidationOrganization validationOrganization;
    private ValidationAddress validationAddress;

    public BuilderOrganization() {
        this.validationAddress = new ValidationAddress();
        this.validationOrganization = new ValidationOrganization();
        this.validationOrganizationType = new ValidationOrganizationType();
    }

    public Object data(OrganizationData organizationData) {
        List<MessageExceptionValidation> errors = new ArrayList<>();
        validationOrganizationType.validationOrganizationType(organizationData.getType().getId())
                .ifPresent(errors::add);
        validationAddress.validation(organizationData.getOfficialAddress().getStreet()).ifPresent(errors::add);
        validationOrganization.validationAnnualTurnover(organizationData.getAnnualTurnover()).ifPresent(errors::add);
        if (errors.isEmpty()) {
            return organizationData;
        } else {
            return new ValidationError(errors, organizationData);
        }
    }
}
