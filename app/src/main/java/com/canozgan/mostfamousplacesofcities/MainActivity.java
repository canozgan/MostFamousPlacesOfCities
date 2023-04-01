package com.canozgan.mostfamousplacesofcities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.canozgan.mostfamousplacesofcities.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    ActivityMainBinding binding;
    ArrayList<Post> postList;
    PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList=new ArrayList<>();
        postAdapter=new PostAdapter(postList);
        binding.recyclerView.setAdapter(postAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.sign_out){
            firebaseFirestore.terminate();
            auth.signOut();
            Intent intentToAuthenticationActivity =new Intent(MainActivity.this,AuthenticationActivity.class);
            startActivity(intentToAuthenticationActivity);
            finish();
        }else{
            Intent intentToMapsActivity =new Intent(MainActivity.this,MapsActivity.class);
            intentToMapsActivity.putExtra("value","add");
            startActivity(intentToMapsActivity);
        }
        return super.onOptionsItemSelected(item);
    }
    public void getData(View view){
        String filter=binding.filter.getText().toString();
        firebaseFirestore.collection("Posts").whereEqualTo("city",filter)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if(value!=null){
                    postList.clear();
                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String,Object> map=snapshot.getData();
                        String email=(String) map.get("email");
                        String downloadUrl=(String) map.get("downloadUrl");
                        String comment=(String) map.get("comment");
                        String placeName=(String) map.get("placeName");
                        String city =(String) map.get("city");
                        Double latitute = Double.valueOf(map.get("latitute").toString()) ;
                        Double longitute=Double.valueOf(map.get("longitute").toString()) ;
                        postList.add(new Post(email,comment,downloadUrl,placeName,city,latitute,longitute));
                    }
                    binding.recyclerView.setAdapter(postAdapter);
                }
            }
        });
    }
}