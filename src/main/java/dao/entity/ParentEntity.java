package dao.entity;

import lombok.*;


/**
 * Сущность радителя
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ParentEntity {
    private final Long id;
    private final Long version;
    private String name;
    private Long address;
}
