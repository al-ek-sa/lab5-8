package edu.itmo.piikt.server.WorkerObject;

import edu.itmo.piikt.common.data.MessageExceptionValidation;
import edu.itmo.piikt.common.data.WorkerData;
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

    public BuilderWorker() {
        this.validationAddress = new ValidationAddress();
        this.validationWorker = new ValidationWorker();
        this.validationStatus = new ValidationStatus();
        this.validationOrganizationType = new ValidationOrganizationType();
        this.validationOrganization = new ValidationOrganization();
        this.validationAddress = new ValidationAddress();
        this.validationCoordinates = new ValidationCoordinates();
    }

    // todo отредактировать, может словить NullPointer
    public Object data(WorkerData workerData) {
        WorkerData data = workerData;
        List<MessageExceptionValidation> errors = new ArrayList<>();
        validationWorker.validationName(workerData.getName()).ifPresent(error -> {
            errors.add(error);
            data.setName(null);
        });
        validationWorker.validationSalary(workerData.getSalary()).ifPresent(error -> {
            errors.add(error);
            data.setSalary(null);
        });
        validationWorker.validationEndDate(workerData.getEndDate()).ifPresent(error -> {
            errors.add(error);
            data.setEndDate(null);
        });
        validationWorker.validationStartDate(workerData.getStartDate()).ifPresent(error -> {
            errors.add(error);
            data.setStartDate(null);
        });
        validationCoordinates.validationX(workerData.getCoordinates().getX()).ifPresent(error -> {
            errors.add(error);
            data.getCoordinates().setX(null);
        });
        validationCoordinates.validationY(workerData.getCoordinates().getY()).ifPresent(error -> {
            errors.add(error);
            data.getCoordinates().setY(null);
        });
        validationStatus.validationStatus(workerData.getStatus().getId()).ifPresent(error -> {
            errors.add(error);
            data.getStatus().setId(null);
        });
        if (workerData.getOrganization() != null) {
            validationOrganization.validationAnnualTurnover(workerData.getOrganization().getAnnualTurnover())
                    .ifPresent(error -> {
                        errors.add(error);
                        data.getOrganization().setAnnualTurnover(null);
                    });
            validationOrganizationType.validationOrganizationType(workerData.getOrganization().getType().getId())
                    .ifPresent(error -> {
                        errors.add(error);
                        data.getOrganization().getType().setId(null);
                    });
            validationAddress.validation(workerData.getOrganization().getOfficialAddress().getStreet())
                    .ifPresent(error -> {
                        errors.add(error);
                        data.getOrganization().getOfficialAddress().setStreet(null);
                    });
        }
        if (errors.isEmpty()) {
            return workerData;
        } else {
            return new ValidationError(errors, data);
        }
    }
}
