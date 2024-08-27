package FITPET.dev.common.enums;

import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 품종
@Getter
@RequiredArgsConstructor
public enum PetType {
    DOG, CAT;

    public static boolean isDog(PetType petType){
        return petType == DOG;
    }

    public static PetType getPetType(String petType){
        if (petType.equals("DOG"))
            return DOG;
        else if (petType.equals("CAT"))
            return CAT;
        else
            throw new GeneralException(ErrorStatus.INVALID_PET_TYPE);
    }
}
