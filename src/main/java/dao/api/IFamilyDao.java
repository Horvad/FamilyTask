package dao.api;

import dao.entity.FamilyEntity;

/**
 * Данный интерфейс предназначен для получения вех данных из хранилища относящихся
 * определенному адресу, ребенку или родителю по id
 */
public interface IFamilyDao {
    /**
     * Получение всех данных касающихся родителя с заданным id
     * @param id
     * @return
     */
    FamilyEntity getFromParent(long id);

    /**
     * Получение всех данных касающихся ребенка с заданным id
     * @param id
     * @return
     */
    FamilyEntity getFromChildren(long id);

    /**
     * Получение всех данных касающихся адреса с заданным id
     * @param id
     * @return
     */
    FamilyEntity getFromAddress(long id);
}
