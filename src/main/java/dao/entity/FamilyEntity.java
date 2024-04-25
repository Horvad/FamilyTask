package dao.entity;

import lombok.*;

import java.util.List;

/***
 * Сущость которая предназначено для отображения полной информации при запросах
 * получания сущности адреса, родителя или ребенко и всей информации, которая к ним относится
 */
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
