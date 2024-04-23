package core.viewDTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AddressViewDTO {
    private long id;
    private long version;
    private String street;
    private int numberHouse;
    private int numberFlat;
}
