package edu.itmo.piikt.common.data.OrganizationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TypeOrganizationDate implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
}
