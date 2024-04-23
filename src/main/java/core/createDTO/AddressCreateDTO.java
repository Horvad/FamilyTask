package core.createDTO;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
public class AddressCreateDTO {
    private String street;
    private int numberHouse;
    private int numberFlat;
}
