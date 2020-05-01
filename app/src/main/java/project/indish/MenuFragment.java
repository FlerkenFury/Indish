package project.indish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import project.indish.adapter.MenuIngredientsAdapter;
import project.indish.adapter.MenuProcedureAdapter;

public class MenuFragment extends AppCompatActivity {

    private ArrayList<String> stepList = new ArrayList<>();
    private ArrayList<String> ingredientsAmountList = new ArrayList<>();
    private ArrayList<String> ingredientsUnitList = new ArrayList<>();
    private ArrayList<String> ingredientsNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        initIngredients();
        initProcedure();

        RecyclerView recyclerViewI = findViewById(R.id.menu_ingredients);
        MenuIngredientsAdapter adapterI = new MenuIngredientsAdapter(ingredientsAmountList, ingredientsUnitList, ingredientsNameList);
        recyclerViewI.setAdapter(adapterI);
        recyclerViewI.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView recyclerViewP = findViewById(R.id.menu_procedure);
        MenuProcedureAdapter adapterP = new MenuProcedureAdapter(stepList, this);
        recyclerViewP.setAdapter(adapterP);
        recyclerViewP.setLayoutManager(new LinearLayoutManager(this));
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
