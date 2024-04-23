package dao.api;

import dao.entity.FamilyEntity;

public interface IFamilyDao {
    FamilyEntity getFromParent(long id);
    FamilyEntity getFromChildren(long id);
    FamilyEntity getFromAddress(long id);
}
