package core;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
public class FamilyAddressDTO {
    private String street;
    private long numberHouse;
    private long numberFlat;
    private List<String> parents;
    private List<String> childrens;
}
