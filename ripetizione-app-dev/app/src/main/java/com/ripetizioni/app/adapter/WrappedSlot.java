package com.ripetizioni.app.adapter;

import com.ripetizioni.app.model.Slot;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class WrappedSlot extends ExpandableGroup<Slot> implements Comparable<WrappedSlot> {

    public WrappedSlot(String title, List<Slot> items) {
        super(title, items);
    }

    @Override
    public int compareTo(WrappedSlot o) {
        return 0;
    }
}
