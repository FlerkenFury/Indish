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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
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

public class ChangeNameFragment extends Fragment {

    private static final String TAG = "ChangeNameFragment";

    private TextInputLayout newName;

    private DatabaseReference mUserRef;

    public ChangeNameFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_change_name, container, false);
        super.onCreate(savedInstanceState);

        final Button btnSaveChangePass = view.findViewById(R.id.btn_save_change_name);
        newName = view.findViewById(R.id.change_name_layout);
        final SharedPref sharedPref =  new SharedPref(getContext());
        final User user = sharedPref.load();

        mUserRef = FirebaseDatabase.getInstance().getReference("user");

        btnSaveChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EditText newName = view.findViewById(R.id.new_name);
//                String newName1 = newName.getText().toString();
                Toast.makeText(getContext(), "change name success", Toast.LENGTH_LONG).show();

                Log.d(TAG, "onClick: "+ newName.getEditText().getText().toString().trim());

                user.setName(newName.getEditText().getText().toString().trim());
                sharedPref.save(user);

                mUserRef.child(user.getUID()).child("name").setValue(user.getName());

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
        });

        return view;
    }

}
