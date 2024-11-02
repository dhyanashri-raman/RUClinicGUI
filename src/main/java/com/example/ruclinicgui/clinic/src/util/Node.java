package com.example.ruclinicgui.clinic.src.util;

import com.example.ruclinicgui.clinic.src.Technician;

/**
 * This class represents a node in a linked list,
 * which stores a reference to a Technician and
 * a reference to the next node in the list.
 * @author Nithya Konduru, Dhyanashri Raman
 */

public class Node {
    Technician technician;
    Node next;

    /**
     * Constructor for the Node class.
     * Initializes the node with a Technician and sets the next reference to null.
     *
     * @param technician The Technician to be stored in this node.
     */
    public Node(Technician technician) {
        this.technician = technician;
        this.next = null;
    }

    /**
     * Retrieves the Technician stored in this node.
     *
     * @return The Technician associated with this node.
     */
    public Technician getTechnician() {
        return technician;
    }

    /**
     * Sets the Technician for this node.
     *
     * @param technician The Technician to be set for this node.
     */
    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    /**
     * Retrieves the next node in the linked list.
     *
     * @return The next Node in the list.
     */
    public Node getNext() {
        return next;
    }

    /**
     * Sets the next node in the linked list.
     *
     * @param next The Node to be set as the next node.
     */
    public void setNext(Node next) {
        this.next = next;
    }
}