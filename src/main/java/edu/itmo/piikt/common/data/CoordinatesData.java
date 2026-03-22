package edu.itmo.piikt.common.data;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CoordinatesData implements Serializable {
    private static final long serialVersionUID = 1L;
    private long x;
    private float y;
}
