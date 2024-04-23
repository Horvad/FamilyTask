package core.viewDTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class ParentViewDTO {
    private final long id;
    private final long version;
    private String name;
    private long addressId;
}
