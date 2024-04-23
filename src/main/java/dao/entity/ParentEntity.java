package dao.entity;

import lombok.*;

import java.util.List;

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
