package core;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class FamilyDTOParent {
    private String name;
    private List<String> childrens;
    private String street;
    private long numberHouse;
    private long numberFlat;
}
