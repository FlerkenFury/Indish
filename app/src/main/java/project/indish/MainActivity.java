package project.indish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import project.indish.adapter.IngredientAdapter;
import project.indish.adapter.RecipeCardAdapter;
import project.indish.model.Ingredient;
import project.indish.model.Recipe;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Spinner spinnerIngredient;
    private Button btnSearch;
    private RecyclerView recyclerViewCardRecipe;

    private IngredientAdapter mIngredientAdapter;
    private RecipeCardAdapter mRecipeCardAdapter;

    private List<Ingredient> newIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerIngredient = findViewById(R.id.main_spinner_ingredient);
        btnSearch = findViewById(R.id.btn_main_search);
        recyclerViewCardRecipe = findViewById(R.id.rv_main_recipes);
        newIngredients = new ArrayList<>();

        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference recipeRef = database.getReference("recipe");
        DatabaseReference ingredientRef = database.getReference("ingredient");



        try {
            Field popup = null;
            try {
                popup = Spinner.class.getDeclaredField("mPopup");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinnerIngredient);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException  | IllegalAccessException e) {
            // silently fail...
        }

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

                mIngredientAdapter = new IngredientAdapter(getApplicationContext(), ingredients);
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
                        mRecipeCardAdapter = new RecipeCardAdapter(getApplicationContext(), recipes);
                        recyclerViewCardRecipe.setAdapter(mRecipeCardAdapter);
                        recyclerViewCardRecipe.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });

    }


}
