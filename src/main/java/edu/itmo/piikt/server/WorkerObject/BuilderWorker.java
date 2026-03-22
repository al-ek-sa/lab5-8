package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.validation.builder.ValidationRules;
import edu.itmo.piikt.server.validation.modelValidation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class BuilderWorker implements Serializable {
    private ValidationWorker validationWorker;
    private ValidationStatus validationStatus;
    private ValidationOrganizationType validationOrganizationType;
    private ValidationOrganization validationOrganization;
    private ValidationAddress validationAddress;
    private ValidationCoordinates validationCoordinates;

    public BuilderWorker(){
        this.validationAddress = new ValidationAddress();
        this.validationWorker = new ValidationWorker();
        this.validationStatus = new ValidationStatus();
        this.validationOrganizationType = new ValidationOrganizationType();
        this.validationOrganization = new ValidationOrganization();
        this.validationAddress = new ValidationAddress();
        this.validationCoordinates = new ValidationCoordinates();
    }
    public Object data(WorkerData workerData) {
        List<MessageExceptionValidation> errors = new ArrayList<>();
        validationWorker.validationName(workerData.getName()).ifPresent(errors::add);
        validationWorker.validationSalary(workerData.getSalary()).ifPresent(errors::add);
        validationWorker.validationEndDate(workerData.getEndDate()).ifPresent(errors::add);
        validationWorker.validationStartDate(workerData.getStartDate()).ifPresent(errors::add);
        validationCoordinates.validationX(workerData.getCoordinates().getX()).ifPresent(errors::add);
        validationCoordinates.validationY(workerData.getCoordinates().getY()).ifPresent(errors::add);
        validationStatus.validationStatus(workerData.getStatus().getId()).ifPresent(errors::add);
        if (workerData.getOrganization() != null) {
            validationOrganization.validationAnnualTurnover(workerData.getOrganization().getAnnualTurnover()).ifPresent(errors::add);
            validationOrganizationType.validationOrganizationType(workerData.getOrganization().getType().getId()).ifPresent(errors :: add);
            validationAddress.validation(workerData.getOrganization().getOfficialAddress().getStreet()).ifPresent(errors::add);
        }
        if (errors.isEmpty()) {
            return workerData;
        } else {
            return new ValidationError(errors, workerData);
        }
    }
}
