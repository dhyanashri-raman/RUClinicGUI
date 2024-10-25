package com.example.ruclinicgui.clinic.src.util;

import com.example.ruclinicgui.clinic.src.Technician;

public class Node {
    Technician technician;
    Node next;

    public Node(Technician technician) {
        this.technician = technician;
        this.next = null;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}