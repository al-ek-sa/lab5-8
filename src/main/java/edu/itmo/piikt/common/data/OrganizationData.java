package edu.itmo.piikt.common.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationData implements Serializable {
    private static final long serialVersionUID = 1L;
    //todo документация! обязательно проверить на null, перепроверить анатоции и зависимости подключить
    private String annualTurnover;
    private OrganizationTypeData type;
    private AddressData officialAddress;
}
