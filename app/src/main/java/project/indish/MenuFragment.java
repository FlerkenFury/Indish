package project.indish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import project.indish.adapter.MenuIngredientsAdapter;
import project.indish.adapter.MenuProcedureAdapter;
import project.indish.model.Ingredient;
import project.indish.model.Recipe;
import project.indish.model.Step;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";

    private ArrayList<String> stepList = new ArrayList<>();
    private ArrayList<String> ingredientsAmountList = new ArrayList<>();
    private ArrayList<String> ingredientsUnitList = new ArrayList<>();
    private ArrayList<String> ingredientsNameList = new ArrayList<>();

    private TextView titleTV, descTV;
    private ImageView imageRecipe;
    private Recipe recipe;
    private String recipeName;
    private FragmentActivity ACTIVITY;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference recipeRef = database.getReference("recipe");

    public MenuFragment() {
    }


    public MenuFragment(String recipeName) {
        this.recipeName = recipeName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.menu, container, false);

        super.onCreate(savedInstanceState);
//        initIngredients();
//        initProcedure();

        ACTIVITY = getActivity();

        titleTV = view.findViewById(R.id.menu_name);
        descTV = view.findViewById(R.id.menu_desc);
        imageRecipe = view.findViewById(R.id.menu_image);
//
//        Intent intent = getActivity().getIntent();
//        final String recipeName =  intent.getStringExtra("name");


        recipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Recipe recipeTemp = postSnapshot.getValue(Recipe.class);

                    // here you can access to name property like university.name

                    Log.d(TAG, "onDataChange: recipeTemp: " + recipeTemp.getName());
                    if (recipeTemp.getName().trim().equalsIgnoreCase(recipeName.trim())){
                        recipe = recipeTemp;


                        if (recipe.getIngredient() != null)
                            for (Ingredient item:
                                    recipe.getIngredient()) {
                                if (item.getName() != null && !item.getName().trim().equalsIgnoreCase("")){
                                    ingredientsNameList.add(item.getName().trim());
                                }
                                else {
                                    ingredientsNameList.add("");
                                }

                                if (item.getUnit() != null && !item.getUnit().trim().equalsIgnoreCase("")) {
                                    ingredientsUnitList.add(item.getUnit().trim());
                                }
                                else {
                                    ingredientsUnitList.add("");
                                }

                                if (item.getAmount() != null && !item.getAmount().trim().equalsIgnoreCase("")) {
                                    ingredientsAmountList.add(item.getAmount().trim());
                                }
                                else {
                                    ingredientsAmountList.add("");
                                }
                            }

                        if (recipe.getStep() != null)
                            for (Step item:
                                    recipe.getStep()) {
                                if (item.getDescription() != null && !item.getDescription().trim().equalsIgnoreCase("")){
                                    stepList.add(item.getDescription());
                                }
                            }

                        titleTV.setText(recipeName);
                        descTV.setText(recipe.getDescription());
                        Glide.with(ACTIVITY)
                                .load(recipe.getImage())
                                .placeholder(R.drawable.image_loader)
                                .into(imageRecipe);


                        RecyclerView recyclerViewI = view.findViewById(R.id.menu_ingredients);
                        MenuIngredientsAdapter adapterI = new MenuIngredientsAdapter(ingredientsAmountList, ingredientsUnitList, ingredientsNameList);
                        recyclerViewI.setAdapter(adapterI);
                        recyclerViewI.setLayoutManager(new LinearLayoutManager(getContext()));

                        RecyclerView recyclerViewP = view.findViewById(R.id.menu_procedure);
                        MenuProcedureAdapter adapterP = new MenuProcedureAdapter(stepList, getContext());
                        recyclerViewP.setAdapter(adapterP);
                        recyclerViewP.setLayoutManager(new LinearLayoutManager(getContext()));

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void initIngredientsAmount(){
        ingredientsAmountList.add("0.5");
        ingredientsAmountList.add("4");
        ingredientsAmountList.add("1");
        ingredientsAmountList.add("30");
        ingredientsAmountList.add("3");
        ingredientsAmountList.add("1");
        ingredientsAmountList.add("1");
        ingredientsAmountList.add("");
    }

    private void initIngredientsUnit(){
        ingredientsUnitList.add("cup");
        ingredientsUnitList.add("");
        ingredientsUnitList.add("tsp");
        ingredientsUnitList.add("g");
        ingredientsUnitList.add("tbsp");
        ingredientsUnitList.add("tsp");
        ingredientsUnitList.add("tsp");
        ingredientsUnitList.add("");
    }

    private void initIngredientsName(){
        ingredientsNameList.add("tahini");
        ingredientsNameList.add("eggs");
        ingredientsNameList.add("baking powder");
        ingredientsNameList.add("honey");
        ingredientsNameList.add("Sukrin Gold or brown sugar");
        ingredientsNameList.add("maple extract");
        ingredientsNameList.add("cinnamon");
        ingredientsNameList.add("sesame seeds for garnish");
    }

    private void initIngredients(){
        initIngredientsAmount();
        initIngredientsUnit();
        initIngredientsName();
    }

    private void initProcedure(){
        stepList.add("Beat all ingredients together really well in a blender.");
        stepList.add("Pour mixture in an oiled cake tray (we suggest sesame oil for the oiling) and sprinkle with sesame seeds.");
        stepList.add("Bake for 25-30 minutes at 345F (convection bake is better). Depending on your oven, you may want to turn it around (horizontally) after 15 minutes to ensure even baking.");
        stepList.add("Done, enjoy! We actually eat it with a spoon straight from the cake tray!");
    }
}
