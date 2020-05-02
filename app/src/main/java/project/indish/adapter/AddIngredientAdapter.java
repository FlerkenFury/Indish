package project.indish.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import project.indish.R;
import project.indish.model.Ingredient;
import project.indish.model.Unit;

public class AddIngredientAdapter extends RecyclerView.Adapter<AddIngredientAdapter.ViewHolder> {

    private static final String TAG = "AddIngredientAdapter";

    private LayoutInflater layoutInflater;
    private List<Ingredient> ingredients;
    private Context mContext;
    private UnitAdapter unitAdapter;


    private int index;

    public AddIngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.ingredients = ingredients;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.insert_ingredient, parent, false);
        return new ViewHolder(view);
    }

    public void ingedientsUpdated(Ingredient ingredient) {
        int position = ingredients.indexOf(ingredient);
        this.notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Ingredient ingredient = ingredients.get(position);


        holder.etIngredient.setHint("ingredient");

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients.add(position + 1, new Ingredient());
                notifyItemInserted(position + 1);
                Log.d(TAG, "onClick: add Position: " + position + 1);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredients.size() <= 1)
                    return;
                int index = position;
                ingredients.remove(index);
                notifyItemRemoved(index);
                notifyItemRangeChanged(index, getItemCount());
                Log.d(TAG, "onClick: remove size asdasda: " + index );
            }
        });


    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        EditText etUnit, etIngredient;
        Spinner spinnerUnit;
        ImageView btnAdd, btnDelete;
        ArrayList<Unit> units;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etUnit = itemView.findViewById(R.id.et_unit);
            etIngredient = itemView.findViewById(R.id.et_add_ingredient);
            spinnerUnit = itemView.findViewById(R.id.spinner_unit);
            btnAdd = itemView.findViewById(R.id.btn_add_ingredient);
            btnDelete = itemView.findViewById(R.id.btn_delete_ingredient);


            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference unitRef = database.getReference("unit");
            units = new ArrayList<>();

            // Read from the database
            unitRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapShot:
                            dataSnapshot.getChildren()) {
                        Unit unit = snapShot.getValue(Unit.class);
                        units.add(unit);
                        Log.d(TAG, "onDataChange: unit: " + unit.getName());
                    }

                    unitAdapter = new UnitAdapter(mContext, units);
                    spinnerUnit.setAdapter(unitAdapter);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });



        }
    }

}
