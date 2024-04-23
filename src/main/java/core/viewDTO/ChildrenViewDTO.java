package core.viewDTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ChildrenViewDTO {
    private final long id;
    private final long version;
    private String name;
    private List<Long> idParents;
}
