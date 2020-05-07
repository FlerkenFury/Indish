package project.indish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import project.indish.model.User;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        super.onCreate(savedInstanceState);

        SharedPref sharedPref =  new SharedPref(getContext());
        User user = sharedPref.load();

        TextView name = (TextView)view.findViewById(R.id.name_person);
        name.setText(user.getName());

        final Button btnChangeName = view.findViewById(R.id.btn_change_name);
        final Button btnChangePass = view.findViewById(R.id.btn_change_pass);

        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangeNameFragment()).commit();
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChangePasswordFragment()).commit();
            }
        });

        return view;
    }

}
