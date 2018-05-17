package edu.icesi.dmi.paperhive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {
    ListView floors_list;
    Button add_btn;

    FloorAdapter floorAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        floors_list = findViewById(R.id.home_floors_list);
        add_btn = findViewById(R.id.home_add_btn);

        floorAdapter = new FloorAdapter(this, reference);
        floors_list.setAdapter(floorAdapter);

        firebaseDatabase = firebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Floor floor = new Floor("Piso #1", "Estúdia con tus compañeros de manera comoda");
                String id = reference.push().getKey();
                floor.setId(id);
                reference.child(id).setValue(floor);
            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Floor floor = dataSnapshot.getValue(Floor.class);
                floorAdapter.addFloor(floor);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
