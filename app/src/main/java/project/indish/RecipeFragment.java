package project.indish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import project.indish.adapter.RecipeCardAdapter;
import project.indish.model.Ingredient;
import project.indish.model.Recipe;

public class RecipeFragment extends Fragment {

    private static final String TAG = "RecipeFragment";

    private RecipeCardAdapter mRecipeCardAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        recyclerView = view.findViewById(R.id.rv_recipe);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipeRef = database.getReference("recipe");

        recipeRef.addValueEventListener(new ValueEventListener() {
            ArrayList<Recipe> recipes = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapShot:
                        dataSnapshot.getChildren()) {
                    Recipe recipe = snapShot.getValue(Recipe.class);

                    recipes.add(recipe);


                }
                Log.d(TAG, "size: " + recipes.size() );
                mRecipeCardAdapter = new RecipeCardAdapter(getContext(), recipes);
                recyclerView.setAdapter(mRecipeCardAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
