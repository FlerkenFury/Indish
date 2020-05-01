package project.indish.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import project.indish.R;

public class MenuIngredientsAdapter extends RecyclerView.Adapter<MenuIngredientsAdapter.MenuViewHolderIngredients>{
    private static final String TAG = "MenuIngredientsAdapter";
    private ArrayList<String> mIngredientsList = new ArrayList<>();

    public MenuIngredientsAdapter(ArrayList<String> ingredientsAmountList, ArrayList<String> ingredientsUnitList, ArrayList<String> ingredientsNameList) {

        for (int i=0;i<ingredientsAmountList.size();i++) {
            String str = (i+1) + ")";
            if(!ingredientsAmountList.get(i).equals("")){
                str  = str.concat(" " + ingredientsAmountList.get(i));
            }
            if(!ingredientsUnitList.get(i).equals("")){
                str = str.concat(" " + ingredientsUnitList.get(i));
            }
            if(!ingredientsNameList.get(i).equals("")){
                str = str.concat(" " + ingredientsNameList.get(i));
            }

            mIngredientsList.add(str);
        }
    }

    @NonNull
    @Override
    public MenuViewHolderIngredients onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_ingredient, parent, false);
        MenuViewHolderIngredients holder = new MenuViewHolderIngredients(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolderIngredients holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.ingredient.setText(mIngredientsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mIngredientsList.size();
    }

    public class MenuViewHolderIngredients extends RecyclerView.ViewHolder{

        TextView ingredient;
        public MenuViewHolderIngredients(@NonNull View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.menu_ingredient);
        }
    }
}
