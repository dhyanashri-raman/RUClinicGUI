package com.example.ruclinicgui.clinic.src.util;

import java.util.Iterator;

/**
 * The List class is a generic container for storing elements of type E.
 * It provides basic list operations such as adding, removing, and checking for elements.
 * It also implements the Iterable interface to allow for iteration over its elements.
 * @author Nithya Konduru, Dhyanashri Raman
 * @param <E> The type of elements in this list
 */
public class List<E> implements Iterable<E> {
    private E[] objects;
    private int size;
    int NOT_FOUND = -1;

    /**
     * Default constructor: Initializes a List object with an initial capacity of 4.
     */
    public List() {
        objects = (E[]) new Object[4];
        size = 0;
    }

    /**
     * Method: Finds the index of the specified element in the list.
     *
     * @param e The element to find
     * @return The index of the element, or -1 if not found
     */
    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Method: Increases the capacity of the internal array by 4.
     */
    private void grow() {
        E[] resizedArray = (E[]) new Object[objects.length + 4];
        for (int i = 0; i < size; i++) {
            resizedArray[i] = objects[i];
        }
        objects = resizedArray;
    }

    /**
     * Method: Checks if the list contains the specified element.
     *
     * @param e The element to check for
     * @return true if the element is in the list, false otherwise
     */
    public boolean contains(E e) {
        if (objects == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (objects[i] != null && objects[i].equals(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method: Adds an element to the list, ensuring no duplicates.
     *
     * @param e The element to add
     * @throws IllegalArgumentException if the element is null
     */
    public void add(E e) {
        if (e == null) {
            throw new IllegalArgumentException("Null elements are not allowed.");
        }
        if (size == objects.length) {
            grow();
        }
        if (!contains(e)) {
            objects[size] = e;
            size++;
        }
    }

    public void addAll(E e) {
        for (int i = 0; i < size(); i++) {
            this.add(get(i));
        }
    }

    /**
     * Method: Removes the specified element from the list.
     *
     * @param e The element to remove
     */
    public void remove(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                for (int j = i; j < size - 1; j++) {
                    objects[j] = objects[j + 1];
                }
                objects[size - 1] = null;
                size--;
                return;
            }
        }
    }

    /**
     * Method: Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    public boolean isEmpty() {
        return objects == null || size == 0;
    }

    /**
     * Method: Returns the number of elements in the list.
     *
     * @return The size of the list
     */
    public int size() {
        return size;
    }

    /**
     * Method: Returns an iterator for the list.
     *
     * @return An iterator over the elements in the list
     */
    public Iterator<E> iterator() {
        return new ListIterator<>();
    }

    /**
     * Method: Retrieves the element at the specified index.
     *
     * @param index The index of the element to retrieve
     * @return The element at the specified index
     */
    public E get(int index) {
        return objects[index];
    }

    /**
     * Method: Sets the element at the specified index.
     *
     * @param index The index to set the element at
     * @param e     The element to set
     */
    public void set(int index, E e) {
        if (size > index) {
            objects[index] = e;
        }
    }

    /**
     * Method: Returns the index of the specified element.
     *
     * @param e The element to find the index of
     * @return The index of the element, or -1 if not found
     */
    public int indexOf(E e) {
        return find(e);
    }

    /**
     * The ListIterator class provides an iterator for the List.
     * It allows traversal of the list's elements in a forward direction.
     *
     * @param <E> The type of elements in the list
     */
    private class ListIterator<E> implements Iterator<E> {
        int currIndex = 0;

        /**
         * Method: Checks if there is another element in the list.
         *
         * @return true if there is a next element, false otherwise
         */
        public boolean hasNext() {
            return currIndex < objects.length && objects[currIndex] != null;
        }

        /**
         * Method: Returns the next element in the list.
         *
         * @return The next element
         */
        public E next() {
            E returnNext = (E) objects[currIndex];
            currIndex++;
            return returnNext;
        }
    }
}