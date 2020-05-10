package project.indish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.indish.adapter.MenuIngredientsAdapter;
import project.indish.adapter.MenuProcedureAdapter;
import project.indish.model.Ingredient;
import project.indish.model.Recipe;
import project.indish.model.Step;
import project.indish.model.User;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private ImageView profilePic;
    private ImageView mImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private String imageURL;

    private StorageReference mStorageRef;
    private DatabaseReference mUserRef;


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
        profilePic = (ImageView) view.findViewById(R.id.btn_add_propic);
        mImageView = (ImageView) view.findViewById(R.id.profile_picture);
        name.setText(user.getName());

        if (user.getImage() != null && !user.getImage().trim().isEmpty()){
            Glide.with(getContext())
                    .load(user.getImage())
                    .placeholder(R.drawable.image_loader)
                    .into(mImageView);
        }

        final Button btnChangeName = view.findViewById(R.id.btn_change_name);
        final Button btnChangePass = view.findViewById(R.id.btn_change_pass);

        mStorageRef = FirebaseStorage.getInstance().getReference("profilepic");
        mUserRef = FirebaseDatabase.getInstance().getReference("user");


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

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

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(getContext()).load(mImageUri).into(mImageView);

            uploadImage();

        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(){

        final String imageName = String.valueOf(System.currentTimeMillis());


        if (mImageUri != null) {
            final StorageReference fileRef = mStorageRef.child(imageName + "." + getFileExtension(mImageUri));

            fileRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Upload upload = new Upload("UserUpload", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            imageURL = fileRef.getDownloadUrl().toString();
                            Log.d(TAG, "onSuccess: WEerwrwe " + taskSnapshot.getStorage().getDownloadUrl().toString());

                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString());
                                    imageURL = downloadUrl.toString();

                                    uploadDatabase();
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e);
                        }
                    });
        }
    }

    public void uploadDatabase (){
        SharedPref sharedPref = new SharedPref(getContext());
        User user = sharedPref.load();
        user.setImage(imageURL);
        sharedPref.save(user);
        mUserRef.child(user.getUID()).child("image").setValue(user.getImage());
    }



}
