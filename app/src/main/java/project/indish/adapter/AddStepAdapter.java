package project.indish.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

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
import project.indish.model.Step;
import project.indish.model.Unit;

public class AddStepAdapter extends RecyclerView.Adapter<AddStepAdapter.ViewHolder> {

    private static final String TAG = "AddStepAdapter";

    private LayoutInflater layoutInflater;
    private List<Step> steps;
    private Context mContext;


    public AddStepAdapter(Context mContext, List<Step> steps) {
        this.steps = steps;
        this.mContext = mContext;
        this.layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.insert_step, parent, false);
        return new AddStepAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.etStep.setHint("step " + position + 1);

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                steps.add(position + 1, new Step());
                notifyItemInserted(position + 1);
                Log.d(TAG, "onClick: add Position: " + position + 1);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (steps.size() <= 1)
                    return;
                int index = position;
                steps.remove(index);
                notifyItemRemoved(index);
                notifyItemRangeChanged(index, getItemCount());
                Log.d(TAG, "onClick: remove size asdasda: " + index );
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        EditText etStep;
        ImageView btnAdd, btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etStep = itemView.findViewById(R.id.et_add_step);
            btnAdd = itemView.findViewById(R.id.btn_add_step);
            btnDelete = itemView.findViewById(R.id.btn_delete_step);


        }
    }
}
