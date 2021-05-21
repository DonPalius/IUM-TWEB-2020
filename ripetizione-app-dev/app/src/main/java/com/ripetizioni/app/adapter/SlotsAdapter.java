package com.ripetizioni.app.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;
import com.ripetizioni.app.R;
import com.ripetizioni.app.model.Slot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SlotsAdapter extends RecyclerView.Adapter<SlotsAdapter.SlotViewHolder> {

    public interface SlotSelected {
        void onSlotsSelected(Slot s);
    }

    private ArrayList<WrappedSlot> slots;
    private SlotSelected slotSelected;

    public SlotsAdapter(SlotSelected slotSelected) {
        slots = new ArrayList<>();
        this.slotSelected = slotSelected;
    }

    public void changeData(List<WrappedSlot> slots) {
        this.slots.clear();
        this.slots.addAll(slots);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_list, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        WrappedSlot d = slots.get(position);
        String title = d.getTitle();
        try {
            String format = new SimpleDateFormat("EEE dd MMMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(title));
            holder.day.setText(format);
        } catch (ParseException e) {
            holder.day.setText(title);
        }




        holder.ll.removeAllViews();
        for (Slot slot : d.getItems()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0,0,10,0);
            MaterialButton b = new MaterialButton(holder.ll.getContext());
            b.setLayoutParams(params);
            b.setText(slot.getStart());
            b.setOnClickListener(view -> slotSelected.onSlotsSelected(slot));

            holder.ll.addView(b);
        }
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }




    class SlotViewHolder extends RecyclerView.ViewHolder {
        TextView day;
        FlexboxLayout ll;
        SlotViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            ll = itemView.findViewById(R.id.llSlots);
        }
    }
}
