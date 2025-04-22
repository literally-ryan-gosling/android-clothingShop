package com.example.clothingshop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText phoneEditText, addressEditText, usernameEditText;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static final String TAG = "ProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameEditText = findViewById(R.id.profileUsernameEditText);
        phoneEditText = findViewById(R.id.profilePhoneEditText);
        addressEditText = findViewById(R.id.profileAddressEditText);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loadProfile();
    }

    private void loadProfile() {
        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String phone = documentSnapshot.getString("phone");
                String address = documentSnapshot.getString("shippingAddress");
                String username = documentSnapshot.getString("username");
                String email = documentSnapshot.getString("email");
                String accountType = documentSnapshot.getString("accountType");

                phoneEditText.setText(phone != null ? phone : "");
                addressEditText.setText(address != null ? address : "");
                usernameEditText.setText(username != null ? username : "");

                EditText emailEditText = findViewById(R.id.profileEmailEditText);
                EditText accountTypeEditText = findViewById(R.id.profileAccountTypeEditText);
                emailEditText.setText(email != null ? email : "");
                accountTypeEditText.setText(accountType != null ? accountType : "");
            }
        });
    }

    public void saveProfile(View view) {
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String username = usernameEditText.getText().toString();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("phone", phone);
        userMap.put("shippingAddress", address);
        userMap.put("username", username);

        String uid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(uid)
                .update(userMap)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profil mentve", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Hiba a mentés során", e);
                    Toast.makeText(this, "Hiba a mentés során", Toast.LENGTH_SHORT).show();
                });
    }

}
