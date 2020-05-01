package project.indish.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import project.indish.R;

public class MenuProcedureAdapter extends RecyclerView.Adapter<MenuProcedureAdapter.MenuViewHolder>{
    private static final String TAG = "MenuProcedureAdapter";
    private ArrayList<String> mStepList = new ArrayList<>();
    private Context mContext;

    public MenuProcedureAdapter(ArrayList<String> stepList, Context context) {
        this.mStepList = stepList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        MenuViewHolder holder = new MenuViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.step.setText(mStepList.get(position));
        holder.parentMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mStepList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStepList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{

        TextView step;
        RelativeLayout parentMenu;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            step = itemView.findViewById(R.id.menu_step);
            parentMenu = itemView.findViewById(R.id.parent_menu);
        }
    }
}
