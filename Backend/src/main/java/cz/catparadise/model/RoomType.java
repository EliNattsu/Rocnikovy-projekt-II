package cz.catparadise.model;

public enum RoomType {
    Pokoj_pro_kočku("Pokoj pro kočku"),
    Pokoj_pro_majitele_a_kočku("Pokoj pro majitele a kočku");

    private final String value;

    RoomType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}