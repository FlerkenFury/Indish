package project.indish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import project.indish.R;
import project.indish.model.Ingredient;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.ingredient_spinner_row, parent, false
            );
        }

        TextView spinnerIngredient = convertView.findViewById(R.id.spinner_ingredient);

        Ingredient currItem = getItem(position);

        if (currItem != null)
            spinnerIngredient.setText(currItem.getName());

        return convertView;
    }

}
