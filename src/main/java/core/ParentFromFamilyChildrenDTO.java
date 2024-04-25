package core;

import lombok.*;

/**
 * Класс является частью класса FamilyDTOChildren {@link FamilyDTOChildren}
 */
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ParentFromFamilyChildrenDTO {
    /**
     * Имя
     */
    private String name;
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
