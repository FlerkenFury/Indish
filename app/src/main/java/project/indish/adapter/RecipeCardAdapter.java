package project.indish.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import project.indish.R;
import project.indish.model.Recipe;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ViewHolder> {

    private static final String TAG = "RecipeCardAdapter";

    private LayoutInflater layoutInflater;
    private List<Recipe> recipes;
    private Context mContext;

    public RecipeCardAdapter(Context context, List<Recipe> recipes) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recipe_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Recipe recipe = recipes.get(position);

        holder.cardTitle.setText(recipe.getName());
        holder.cardDesc.setText(recipe.getDescription());

        if (!recipe.getImage().isEmpty()) {
            Log.d(TAG, "onBindViewHolder: " + recipe.getImage());

            Glide.with(mContext)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.image_loader)
                    .into(holder.cardImage);
        }

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView cardTitle, cardDesc;
        ImageView cardImage;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            cardTitle = itemView.findViewById(R.id.card_recipe_title);
            cardDesc = itemView.findViewById(R.id.card_recipe_desc);
            cardImage = itemView.findViewById(R.id.card_recipe_image);
        }

    }


}
