package core.viewDTO;

import lombok.*;

import java.util.List;

/**
 * Класс предназначен для отображения детей
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ChildrenViewDTO {
    /**
     * id ребенка
     */
    private final long id;
    /**
     * текущая версия
     */
    private final long version;
    /**
     * имя
     */
    private String name;
    /**
     * массив id родителей
     */
    private List<Long> idParents;
}
