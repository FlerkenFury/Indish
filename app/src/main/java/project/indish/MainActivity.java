package project.indish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import project.indish.adapter.IngredientAdapter;
import project.indish.model.Ingredient;
import project.indish.model.Recipe;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Spinner spinnerIngredient;
    private IngredientAdapter mIngredientAdapter;

    private List<Ingredient> newIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerIngredient = findViewById(R.id.main_spinner_ingredient);
        newIngredients = new ArrayList<>();

        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("recipe");


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
            popupWindow.setHeight(1000);
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


    }


}
