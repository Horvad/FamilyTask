package dao.entity;

import lombok.*;

import java.util.List;

/**
 * сущность ребенка
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ChildrenEntity {
    private final Long id;
    private final Long version;
    private String name;
    private List<Long> idParents;
}
