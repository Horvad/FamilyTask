package dao.entity;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AddressEntity {
    @Getter
    private Long id;
    @Getter
    private Long version;
    @Setter
    @Getter
    private String street;
    @Setter
    @Getter
    private int numberFlat;
    @Getter
    @Setter
    private int numberHouse;
}
