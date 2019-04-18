package com.gmail.luxdevpl.fbot.impl.storage.util;

import org.apache.commons.lang3.Validate;

public enum StorageTypes {

    MYSQL("MYSQL"), SQLITE("SQLITE");

    public static StorageTypes fromName(String name) {
        Validate.notNull(name);
        name = name.toLowerCase();

        for (StorageTypes storage : values()) {
            if (storage.getName().equals(name)) {
                return storage;
            }
        }

        return null;
    }

    private final String name;

    StorageTypes(String name) {
        Validate.notNull(name);

        this.name = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

}
