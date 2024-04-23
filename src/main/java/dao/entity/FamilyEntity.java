package dao.entity;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FamilyEntity {
    private List<AddressEntity> address;
    private List<ChildrenEntity> children;
    private List<ParentEntity> parent;
}
