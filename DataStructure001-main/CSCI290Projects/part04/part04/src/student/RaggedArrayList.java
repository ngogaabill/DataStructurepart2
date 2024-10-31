/**
 * File: RaggedArrayList.java
 * ****************************************************************************
 *                           Revision History
 * ****************************************************************************
 *
 * 8/2015 - Anne Applin - Added formatting and JavaDoc
 * 2015 - Bob Boothe - starting code
 * ****************************************************************************
 */
package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * *
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *
 * It keeps the items in sorted order according to the comparator. Duplicates
 * are allowed. New items are added after any equivalent items.
 *
 * NOTE: normally fields, internal nested classes and non API methods should all
 * be private, however they have been made public so that the tester code can
 * set them
 *
 * @author Bob Booth
 * @param <E> A generic data type so that this structure can be built with any
 * data type (object)
 */
public class RaggedArrayList<E> implements Iterable<E> {

    // must be even so when split get two equal pieces
    private static final int MINIMUM_SIZE = 4;
    /**
     * The total number of elements in the entire RaggedArrayList
     */
    public int size;
    /**
     * really is an array of L2Array, but compiler won't let me cast to that
     */
    public Object[] l1Array;
    /**
     * The number of elements in the l1Array that are used.
     */
    public int l1NumUsed;
    /**
     * a Comparator object so we can use compare for Song
     */
    private Comparator<E> comp;

    /**
     * create an empty list always have at least 1 second level array even if
     * empty, makes code easier (DONE - do not change)
     *
     * @param c a comparator object
     */
    public RaggedArrayList(Comparator<E> c) {
        size = 0;
        // you can't create an array of a generic type
        l1Array = new Object[MINIMUM_SIZE];
        // first 2nd level array
        l1Array[0] = new L2Array(MINIMUM_SIZE);
        l1NumUsed = 1;
        comp = c;
    }

    /**
     * ***********************************************************
     * nested class for 2nd level arrays read and understand it. (DONE - do not
     * change)
     */
    public class L2Array {

        /**
         * the array of items
         */
        public E[] items;
        /**
         * number of items in this L2Array with values
         */
        public int numUsed;

        /**
         * Constructor for the L2Array
         *
         * @param capacity the initial length of the array
         */
        public L2Array(int capacity) {
            // you can't create an array of a generic type
            items = (E[]) new Object[capacity];
            numUsed = 0;
        }
    }// end of nested class L2Array

    // ***********************************************************
    /**
     * total size (number of entries) in the entire data structure (DONE - do
     * not change)
     *
     * @return total size of the data structure
     */
    public int size() {
        return size;
    }

    /**
     * null out all references so garbage collector can grab them but keep
     * otherwise empty l1Array and 1st L2Array (DONE - Do not change)
     */
    public void clear() {
        size = 0;
        // clear all but first l2 array
        Arrays.fill(l1Array, 1, l1Array.length, null);
        l1NumUsed = 1;
        L2Array l2Array = (L2Array) l1Array[0];
        // clear out l2array
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null);
        l2Array.numUsed = 0;
    }

    /**
     * *********************************************************
     * nested class for a list position used only internally 2 parts: level 1
     * index and level 2 index
     */
    public class ListLoc {

        /**
         * Level 1 index
         */
        public int level1Index;

        /**
         * Level 2 index
         */
        public int level2Index;

        /**
         * Parameterized constructor DONE (Do Not Change)
         *
         * @param level1Index input value for property
         * @param level2Index input value for property
         */
        public ListLoc(int level1Index, int level2Index) {
            this.level1Index = level1Index;
            this.level2Index = level2Index;
        }

        /**
         * test if two ListLoc's are to the same location (done -- do not
         * change)
         *
         * @param otherObj the other listLoc
         * @return true if they are the same location and false otherwise
         */
        public boolean equals(Object otherObj) {
            // not really needed since it will be ListLoc
            if (getClass() != otherObj.getClass()) {
                return false;
            }
            ListLoc other = (ListLoc) otherObj;

            return level1Index == other.level1Index
                    && level2Index == other.level2Index;
        }

        /**
         * move ListLoc to next entry when it moves past the very last entry it
         * will be one index past the last value in the used level 2 array. Can
         * be used internally to scan through the array for sublist also can be
         * used to implement the iterator.
         */
        public void moveToNext() {
            // TO DO IN PART 5 and NOT BEFORE
        }
    }

    /**
     * /**
     * Finds the location of the specified item in the Ragged ArrayList. If the
     * item exists, returns the location of the first occurrence. If the item
     * does not exist, returns the location where it should be inserted.
     *
     * @param item The item to search for.
     * @return A ListLoc representing the item's location or the insertion
     * point.
     */
    public ListLoc findFront(E item) {
        int l1 = 0;
        if (l1NumUsed == 0 || ((L2Array) l1Array[l1]).numUsed == 0) {
            return new ListLoc(0, 0);
        }
        // First loop: Find the L2Array that may contain the element
        while (l1 < l1NumUsed && l1Array[l1] != null && comp.compare(((L2Array) l1Array[l1]).items[((L2Array) l1Array[l1]).numUsed - 1], item) < 0) {
            l1++;
        }
        if (l1Array[l1] == null) {
            l1--;
        }
        // If at an empty or larger L1 array, item should go in the first position
        if (l1 == l1NumUsed || comp.compare(((L2Array) l1Array[l1]).items[0], item) > 0) {
            return new ListLoc(l1, 0); // Insert at the start of this L2Array
        }

        // Second loop: Traverse through the L2Array to find the exact position
        L2Array l2Array = (L2Array) l1Array[l1];
        int l2 = 0;
        while (l2 < l2Array.numUsed && comp.compare(l2Array.items[l2], item) < 0) {
            l2++;
        }

        return new ListLoc(l1, l2); // Return the location of the item
    }

    public ListLoc findEnd(E item) {
        int l1 = l1NumUsed - 1;
        if (l1 == -1 || ((L2Array) l1Array[l1]).numUsed == 0) {
            return new ListLoc(0, 0);
        }

        // First loop: Find the L2Array that may contain the element
        while (l1 > 0 && l1Array[l1] != null && comp.compare(((L2Array) l1Array[l1]).items[0], item) > 0) {
            l1--;
        }
        // If all elements of the l2 array should go after the item, return the 0 index of the l2 array
        if (l1 == l1NumUsed || comp.compare(((L2Array) l1Array[l1]).items[0], item) > 0) {
            return new ListLoc(l1, 0); // Insert at the start of this L2Array
        }

        // Second loop: Traverse through the L2Array backwards to find the exact position
        L2Array l2Array = (L2Array) l1Array[l1];
        int l2 = l2Array.numUsed - 1;
        while (l2 >= 0 && comp.compare(l2Array.items[l2], item) > 0) {
            l2--;
        }
        l2++;

        return new ListLoc(l1, l2); // Return the location of the item

    }
    /**
     * add object after any other matching values findEnd will give the
     * insertion position
     *
     * @param item the thing we are searching for a place to put.
     * @return
     */
    
    public boolean add(E item) {
        ListLoc idx = findEnd(item);//find insertion point
        L2Array l2Array = (L2Array) l1Array[idx.level1Index];
        // move up to insert
        int pos = idx.level2Index;
        System.arraycopy(l2Array.items, pos, l2Array.items, pos+1,
                l2Array.numUsed-pos);
        l2Array.items[pos] = item;//insert at calculated position
        l2Array.numUsed++;
        size++;

        // Check if full
        if (l2Array.numUsed < l2Array.items.length)
            return true;    // not full 

        // grow or split block
        int l2Length = l2Array.items.length;

        if (l2Length  < l1Array.length) { // small block, just double it
            l2Array.items = Arrays.copyOf(l2Array.items, l2Length*2);
        } else {                                    // large block, split it
            L2Array l2ArrayB = new L2Array(l2Length);
            System.arraycopy(l2Array.items, l2Length/2, l2ArrayB.items,
                    0, l2Length/2);  // copy top half
            // fill old top with nulls
            Arrays.fill(l2Array.items, l2Length/2, l2Length, null);
            l2Array.numUsed = l2Length/2;
            l2ArrayB.numUsed = l2Length/2;
            // now insert into l1Array
            int posl1 = idx.level1Index+1;   // index of inserted block B
            System.arraycopy(l1Array, posl1, l1Array, posl1+1, l1NumUsed-posl1);
            l1Array[posl1] = l2ArrayB;
            l1NumUsed++;
            // is level 1 array full? then double it
            if (l1NumUsed == l1Array.length) {
                l1Array = Arrays.copyOf(l1Array, l1NumUsed*2);
            }
        }
        return true;
    }
  
    

    /**
     * siginal list is unaffected findStart and findEnd will be useful here
     *
     * @param fromElement the starting element
     * @param toElement the element after the last element we actually want
     * @return the sublist
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        // TO DO in part 5 and NOT BEFORE

        RaggedArrayList<E> result = new RaggedArrayList<E>(comp);
        return result;
    }

    /**
     * returns an iterator for this list this method just creates an instance of
     * the inner Itr() class (DONE)
     *
     * @return an iterator
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Iterator is just a list loc. It starts at (0,0) and finishes with index2
     * 1 past the last item in the last block
     */
    private class Itr implements Iterator<E> {

        private ListLoc loc;

        /*
         * create iterator at start of list
         * (DONE)
         */
        Itr() {
            loc = new ListLoc(0, 0);
        }

        /**
         * check to see if there are more items
         */
        public boolean hasNext() {
            // TO DO in part 5 and NOT BEFORE

            return false;
        }

        /**
         * return item and move to next throws NoSuchElementException if off end
         * of list. An exception is thrown here because calling next() without
         * calling hasNext() shows a certain level or stupidity on the part of
         * the programmer, so it can blow up. They deserve it.
         */
        public E next() {
            // TO DO in part 5 and NOT BEFORE

            throw new IndexOutOfBoundsException();
        }

        /**
         * Remove is not implemented. Just use this code. (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
