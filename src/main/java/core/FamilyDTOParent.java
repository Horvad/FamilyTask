package core;

import lombok.*;

import java.util.List;

/**
 * Класс предназначен для вывода родителя, имен его детей и адреса проживания
 */
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class FamilyDTOParent {
    /**
     * Имя
     */
    private String name;
    /**
     * масси имен детей
     */
    private List<String> childrens;
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
}
