package service.api;

import core.FamilyAddressDTO;
import core.FamilyDTOChildren;
import core.FamilyDTOParent;

/**
 * интерфейс предназначен для получения полной информации по id из хранилища и отправку к
 * конечному пользователю (servlet, telegram и.т.д)
 */
public interface IFamilyService {
    /**
     * получение всех данных о родителе по id
     * @param id
     * @return
     */
    FamilyDTOParent getFromParent(long id);

    /**
     * получение всех данный о ребенке по id
     * @param id
     * @return
     */
    FamilyDTOChildren getFromChildren(long id);

    /**
     * получение всех данный о адресе по id
     * @param id
     * @return
     */
    FamilyAddressDTO getFromAddress(long id);
}
