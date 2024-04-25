package core.createDTO;

import lombok.*;

import java.util.List;

/**
 * Клис предначначен для создания пользователем детей
 */
@AllArgsConstructor
@Setter
@Getter
public class ChildrenCreateDTO {
    /**
     * Имя ребенка
     */
    private String name;
    /**
     * id родителей
     */
    private List<Long> idParent;
}
