package core.createDTO;

import lombok.*;

/**
 * Класс предначночан для создания пользователем адресов
 */
@AllArgsConstructor
@Setter
@Getter
public class AddressCreateDTO {
    /**
     * Улица адреса
     */
    private String street;
    /**
     * Номер дома
     */
    private int numberHouse;
    /**
     * Номер квартиры
     */
    private int numberFlat;
}
