package project.indish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import project.indish.adapter.AddIngredientAdapter;
import project.indish.adapter.AddStepAdapter;
import project.indish.model.Ingredient;
import project.indish.model.Recipe;
import project.indish.model.Step;
import project.indish.model.Unit;

import static android.app.Activity.RESULT_OK;

public class AddRecipeFragment extends Fragment {

    private static final String TAG = "AddRecipeFragment";
    
    public static List<Ingredient> ingredients;
    private List<Step> steps;
    private RecyclerView ingredientRecyclerView, stepRecyclerView;
    private AddIngredientAdapter addIngredientAdapter;
    private AddStepAdapter addStepAdapter;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage, mBtnSubmit;
    private ImageView mImageView;
    private EditText titleET, descET;
    private String imageURL;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mRecipeRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_recipe_fragment, container, false);

        ingredientRecyclerView = view.findViewById(R.id.rv_add_ingredient);
        ingredients = new ArrayList<>();
        ingredients.add(new Ingredient());

        addIngredientAdapter = new AddIngredientAdapter(getContext(), ingredients);
        ingredientRecyclerView.setAdapter(addIngredientAdapter);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        stepRecyclerView = view.findViewById(R.id.rv_steps);
        steps = new ArrayList<>();
        steps.add(new Step());

        addStepAdapter = new AddStepAdapter(getContext(), steps);
        stepRecyclerView.setAdapter(addStepAdapter);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mButtonChooseImage = view.findViewById(R.id.btn_upload_image);
        mImageView = view.findViewById(R.id.iv_upload_image);
        titleET = view.findViewById(R.id.et_title);
        descET = view.findViewById(R.id.et_desc);

        mBtnSubmit = view.findViewById(R.id.btn_insert);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mRecipeRef = FirebaseDatabase.getInstance().getReference("recipe");


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
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
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }
    }

    private void uploadFile(){

        uploadImage();

    }

    private void uploadDatabase(){
        Recipe recipe = new Recipe();

        List<Ingredient> ingredientList = new ArrayList<>();
        List<Step> stepList = new ArrayList<>();

        for (int i = 0; i< ingredients.size(); i ++) {
            Log.d(TAG, "uploadFile: " + i);
            View childView = ingredientRecyclerView.getChildAt(i);

            EditText amountEditText = (EditText) childView.findViewById(R.id.et_unit);
            EditText nameEditText = (EditText) childView.findViewById(R.id.et_add_ingredient);
            Spinner unitSpinner = (Spinner) childView.findViewById(R.id.spinner_unit);
            String ingredientName = nameEditText.getText().toString().trim();
            String unitAmount = amountEditText.getText().toString().trim();
            Unit unit = (Unit) unitSpinner.getSelectedItem();
            String unitName = unit.getName().trim();

            Log.d(TAG, "uploadFile: spinner: " + unitName);

            Ingredient tempIngredient = new Ingredient();
            tempIngredient.setName(ingredientName);
            Log.d(TAG, "uploadFile: name: " + ingredientName);
            tempIngredient.setAmount(unitAmount);
            tempIngredient.setUnit(unitName);

            ingredientList.add(tempIngredient);
        }

        for (int i = 0; i < steps.size(); i++) {
            View childView = stepRecyclerView.getChildAt(i);

            EditText stepEditText = (EditText) childView.findViewById(R.id.et_add_step);

            String stepName = stepEditText.getText().toString().trim();

            Step tempStep = new Step();
            tempStep.setDescription(stepName);
            Log.d(TAG, "uploadFile: step: " + stepName);

            stepList.add(tempStep);

        }

//        uploadImage();
        Log.d(TAG, "uploadFile: url: " + imageURL);

        String title = titleET.getText().toString().trim();
        String description = descET.getText().toString().trim();

        recipe.setName(title);
        recipe.setDescription(description);
        recipe.setIngredient(ingredientList);
        recipe.setStep(stepList);
        recipe.setImage(imageURL);

        String uploadID = mRecipeRef.push().getKey();
        mRecipeRef.child(uploadID).setValue(recipe);

        Intent intent = new Intent(getContext(), HomeDrawerActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

}


