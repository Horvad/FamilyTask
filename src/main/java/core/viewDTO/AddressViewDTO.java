package core.viewDTO;

import lombok.*;

/**
 * Класс предназначен для отображения пользователю адреса
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AddressViewDTO {
    /**
     * id авдреса
     */
    private long id;
    /**
     * текущая версия адреса
     */
    private long version;
    /**
     * Улица
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
