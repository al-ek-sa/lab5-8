package edu.itmo.piikt.common.data;
import edu.itmo.piikt.common.models.Coordinates;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.common.models.Status;
import lombok.*;
import java.io.Serializable;

/**
 * The class of the Worker type object.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class WorkerData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Coordinates coordinates;
    private Float salary;
    private java.time.LocalDate startDate;
    private java.time.ZonedDateTime endDate;
    private Status status;
    private Organization organization;
}
