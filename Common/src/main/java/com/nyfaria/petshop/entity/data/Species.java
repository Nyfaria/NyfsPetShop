package com.nyfaria.petshop.entity.data;

public enum Species {
    DOG("dog"),
    CAT("cat"),
    BIRD("bird");
    final String name;

    Species(String name) {
        this.name = name;
    }

    public static Species getByName(String name) {
        for (Species species : values()) {
            if (species.getName().equals(name)) {
                return species;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
