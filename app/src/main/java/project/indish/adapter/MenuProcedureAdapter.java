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

public class MenuProcedureAdapter extends RecyclerView.Adapter<MenuProcedureAdapter.MenuViewHolderProcedure>{
    private static final String TAG = "MenuProcedureAdapter";
    private ArrayList<String> mStepList = new ArrayList<>();
    private Context mContext;

    public MenuProcedureAdapter(ArrayList<String> stepList, Context context) {
        this.mStepList = stepList;
        for (int i=0;i<stepList.size();i++) {
            mStepList.set(i, (i+1) + ") " + mStepList.get(i));
        }

        this.mContext = context;
    }

    @NonNull
    @Override
    public MenuViewHolderProcedure onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_procedure, parent, false);
        MenuViewHolderProcedure holder = new MenuViewHolderProcedure(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolderProcedure holder, final int position) {
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

    public class MenuViewHolderProcedure extends RecyclerView.ViewHolder{

        TextView step;
        RelativeLayout parentMenu;
        public MenuViewHolderProcedure(@NonNull View itemView) {
            super(itemView);
            step = itemView.findViewById(R.id.menu_step);
            parentMenu = itemView.findViewById(R.id.parent_menu);
        }
    }
}
