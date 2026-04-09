package edu.itmo.piikt.common.data.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
}
