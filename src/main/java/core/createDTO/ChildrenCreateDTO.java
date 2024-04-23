package core.createDTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class ChildrenCreateDTO {
    private String name;
    private List<Long> idParent;
}
