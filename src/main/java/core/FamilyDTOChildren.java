package core;

import lombok.*;

import java.util.List;

/**
 * Класс предназначн для отображения ребенка
 * и всех иго родителей(включая адреса проживания родителей)
 */
@AllArgsConstructor
@Getter
public class FamilyDTOChildren {
    /**
     * имя
     */
    private String name;
    /**
     * информация о родителе см. {@link ParentFromFamilyChildrenDTO}
     */
    private List<ParentFromFamilyChildrenDTO> parrents;
}
