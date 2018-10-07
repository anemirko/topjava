package ru.javawebinar.topjava.model;

public interface HasId {
    Integer getId();

    default boolean isNew() {
        return getId() == null;
    }
}
