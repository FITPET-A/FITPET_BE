package FITPET.dev.common.enums;

// 품종
public enum PetType {
    DOG, CAT;

    public boolean isDog(PetType petType){
        return petType == DOG;
    }
}
