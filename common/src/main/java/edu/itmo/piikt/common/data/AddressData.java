package edu.itmo.piikt.common.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public final class AddressData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String street;
}
