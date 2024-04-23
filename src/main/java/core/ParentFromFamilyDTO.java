package core;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ParentFromFamilyDTO {
    private String name;
    private String street;
    private long numberHouse;
    private long numberFlat;
}
