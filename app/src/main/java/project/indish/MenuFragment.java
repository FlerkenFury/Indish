package project.indish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import project.indish.adapter.MenuProcedureAdapter;

public class MenuFragment extends AppCompatActivity{

    private ArrayList<String> stepList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        initProcedure();
    }

    private void initProcedure(){
        stepList.add("Beat all ingredients together really well in a blender.");
        stepList.add("Pour mixture in an oiled cake tray (we suggest sesame oil for the oiling) and sprinkle with sesame seeds.");
        stepList.add("Bake for 25-30 minutes at 345F (convection bake is better). Depending on your oven, you may want to turn it around (horizontally) after 15 minutes to ensure even baking.");
        stepList.add("Done, enjoy! We actually eat it with a spoon straight from the cake tray!");
        initProcedureRecyclerView();
    }

    private void initProcedureRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.menu_procedure);
        MenuProcedureAdapter adapter = new MenuProcedureAdapter(stepList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
