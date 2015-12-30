package com.visiontech.yummysmile.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.repository.api.dto.MealDTO;
import com.visiontech.yummysmile.util.Constants;

import java.util.Collections;
import java.util.List;

/**
 * @author hector.torres
 */
public class MainCardsAdapter extends RecyclerView.Adapter<MainCardsAdapter.MainCardsViewHolder> {

    private List<MealDTO> meals = Collections.emptyList();
    private Context context;
    private static MealCardOnClickListener listener;

    public MainCardsAdapter(Context context, List<MealDTO> meals, MealCardOnClickListener listener) {
        this.context = context;
        this.meals = meals;
        this.listener = listener;
    }

    @Override
    public MainCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardsViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(MainCardsViewHolder holder, int position) {
        final MealDTO meal = meals.get(position);
        holder.setData(meal);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMealCardClicked(meal);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }


    public void clear() {
        meals.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<MealDTO> items) {
        meals.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Interface that fires the Meal Card click event.
     */
    public interface MealCardOnClickListener {
        void onMealCardClicked(MealDTO mealDTO);
    }


    public class MainCardsViewHolder extends RecyclerView.ViewHolder {

        private TextView description;
        private ImageView picture;

        public MainCardsViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item, parent, false));

            description = (TextView) itemView.findViewById(R.id.tv_description);
            picture = (ImageView) itemView.findViewById(R.id.iv_picture);
        }

        public void setData(MealDTO mealDTO) {
            description.setText(mealDTO.getName());
            String url = Constants.HOST + Constants.URI + mealDTO.getFileName();
            Glide.with(context).load(url).centerCrop().into(picture);
        }
    }
}
