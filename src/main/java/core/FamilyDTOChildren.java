package core;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
public class FamilyDTOChildren {
    private String name;
    private List<ParentFromFamilyDTO> parrents;
}
