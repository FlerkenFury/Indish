package project.indish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import project.indish.adapter.IngredientAdapter;
import project.indish.adapter.RecipeCardAdapter;
import project.indish.model.Ingredient;
import project.indish.model.Recipe;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private Spinner spinnerIngredient;
    private Button btnSearch;
    private RecyclerView recyclerViewCardRecipe;

    private IngredientAdapter mIngredientAdapter;
    private RecipeCardAdapter mRecipeCardAdapter;

    private List<Ingredient> newIngredients;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        spinnerIngredient = view.findViewById(R.id.main_spinner_ingredient);
        btnSearch = view.findViewById(R.id.btn_main_search);
        recyclerViewCardRecipe = view.findViewById(R.id.rv_main_recipes);
        newIngredients = new ArrayList<>();

        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference recipeRef = database.getReference("recipe");
        DatabaseReference ingredientRef = database.getReference("ingredient");

        // Read from the database
        ingredientRef.addValueEventListener(new ValueEventListener() {
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot:
                        dataSnapshot.getChildren()) {
                    Ingredient ingredient = snapShot.getValue(Ingredient.class);
                    ingredients.add(ingredient);
                }

                mIngredientAdapter = new IngredientAdapter(getContext(), ingredients);
                spinnerIngredient.setAdapter(mIngredientAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String query = spinnerIngredient.getSelectedItem().toString();
                Log.d(TAG, "onClick: " + query);

                recipeRef.addValueEventListener(new ValueEventListener() {
                    ArrayList<Recipe> recipes = new ArrayList<>();

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapShot:
                                dataSnapshot.getChildren()) {
                            Recipe recipe = snapShot.getValue(Recipe.class);

                            if (recipe.getIngredient() != null && !recipe.getIngredient().isEmpty()){
                                Log.d(TAG, "asd: " + recipe.getName());
                                for (Ingredient item:
                                        recipe.getIngredient()) {
                                    if(item.getName() != null && !item.getName().isEmpty() &&  item.getName().equalsIgnoreCase(query)){
                                        recipes.add(recipe);
                                        break;
                                    }
                                }
                            }


                        }
                        Log.d(TAG, "size: " + recipes.size() );
                        mRecipeCardAdapter = new RecipeCardAdapter(getContext(), recipes);
                        recyclerViewCardRecipe.setAdapter(mRecipeCardAdapter);
                        recyclerViewCardRecipe.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });

        return view;

    }


}
