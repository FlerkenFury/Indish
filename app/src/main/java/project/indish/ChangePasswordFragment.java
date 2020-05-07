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

public class ChangePasswordFragment extends Fragment {

    private static final String TAG = "ChangePasswordFragment";

    public ChangePasswordFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        super.onCreate(savedInstanceState);

        final Button btnSaveChangePass = view.findViewById(R.id.btn_save_change_pass);

        btnSaveChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText oldPass = view.findViewById(R.id.old_pass);
                EditText newPass = view.findViewById(R.id.new_pass);
                EditText confirmPass = view.findViewById(R.id.confirm_pass);
                String oldPass1 = oldPass.getText().toString();
                String newPass1 = newPass.getText().toString();
                String confirmPass1 =  confirmPass.getText().toString();
                SharedPref sharedPref =  new SharedPref(getContext());
                User user = sharedPref.load();
                if(oldPass1.equals("")){
                    Toast.makeText(getContext(), "old password can not be empty", Toast.LENGTH_LONG).show();
                }
                else if(newPass1.equals("")){
                    Toast.makeText(getContext(), "new password can not be empty", Toast.LENGTH_LONG).show();
                }
                else if(confirmPass1.equals("")){
                    Toast.makeText(getContext(), "confirm new password can not be empty", Toast.LENGTH_LONG).show();
                }
                else if (!oldPass1.equals(user.getPassword())){
                    Toast.makeText(getContext(), "old password incorrect", Toast.LENGTH_LONG).show();
                }
                else if (!newPass1.equals(confirmPass1)){
                    Toast.makeText(getContext(), "password does not match", Toast.LENGTH_LONG).show();
                }
                else if(newPass1.length() < 6){
                    Toast.makeText(getContext(), "password is too weak", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(), "change password success", Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                }
            }
        });

        return view;
    }

}
