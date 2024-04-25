package core;

import lombok.*;

import java.util.List;

/**
 * Класс предназначен для отображения адреса и именя всех родителей и детей,
 * которые могут проживавать в данном адресе
 */
@AllArgsConstructor
@Getter
public class FamilyAddressDTO {
    /**
     * улица
     */
    private String street;
    /**
     * номер дома
     */
    private long numberHouse;
    /**
     * номер квартиры
     */
    private long numberFlat;
    /**
     * массив имен радителей
     */
    private List<String> parents;
    /**
     * массив имен детей
     */
    private List<String> childrens;
}
