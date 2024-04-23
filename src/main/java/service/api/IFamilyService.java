package service.api;

import core.FamilyAddressDTO;
import core.FamilyDTOChildren;
import core.FamilyDTOParent;

public interface IFamilyService {
    FamilyDTOParent getFromParent(long id);
    FamilyDTOChildren getFromChildren(long id);
    FamilyAddressDTO getFromAddress(long id);
}
