package me.alek.cache;

import java.util.ArrayList;

public abstract class Container<T> {

    private ArrayList<T> containerList;
    private Registery<T> registery;

    public Container() {
        containerList = new ArrayList<>();
        registery = getRegistery();
    }

    public void add(T element) {
        containerList.add(element);
    }

    public ArrayList<T> getList() {
        return containerList;
    }

    public abstract Registery<T> getRegistery();

}
