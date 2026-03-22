package edu.itmo.piikt.common.data;

import java.io.Serializable;

public class OrganizationData implements Serializable {
    private static final long serialVersionUID = 1L;
    //todo документация! обязательно проверить на null, перепроверить анатоции и зависимости подключить
    private String annualTurnover;
    private OrganizationTypeData type;
    private AddressData officialAddress;
}
