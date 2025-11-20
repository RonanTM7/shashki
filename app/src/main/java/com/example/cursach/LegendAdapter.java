package com.example.cursach;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LegendAdapter extends RecyclerView.Adapter<LegendAdapter.LegendViewHolder> {

    private final Context context;
    private final List<Legend> legends;

    public LegendAdapter(Context context, List<Legend> legends) {
        this.context = context;
        this.legends = legends;
    }

    @NonNull
    @Override
    public LegendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_legend, parent, false);
        return new LegendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LegendViewHolder holder, int position) {
        Legend legend = legends.get(position);
        holder.textViewLegendName.setText(legend.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LegendDetailActivity.class);
            intent.putExtra("legend_id", legend.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return legends.size();
    }

    public static class LegendViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLegendName;

        public LegendViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLegendName = itemView.findViewById(R.id.textViewLegendName);
        }
    }
}
