package FITPET.dev.common.enums;

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
}
