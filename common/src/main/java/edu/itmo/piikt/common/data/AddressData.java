package edu.itmo.piikt.common.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Data
@AllArgsConstructor
public final class AddressData implements Serializable {
    private static final long serialVersionUID = 1L;
    // todo документация! обязательно проверить на null, перепроверить анатоции и
    // зависимости подключить
    private String street;
}
