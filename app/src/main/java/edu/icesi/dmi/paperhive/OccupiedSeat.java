package edu.icesi.dmi.paperhive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OccupiedSeat extends AppCompatActivity {
    TextView seat_name_tv, seat_start_time_tv, seat_finish_time_tv;
    Button report_btn;

    int seat_initial_time, seat_finish_time;
    String seat_name, seat_id, seat_floor_id, seat_user;

    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference user_reference;

    ListView report_list;
    ArrayList<String> reports;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occupied_seat);

        seat_name_tv = findViewById(R.id.occupied_seat_seat_name_tv);
        seat_start_time_tv = findViewById(R.id.occupied_seat_start_time_tv);
        seat_finish_time_tv = findViewById(R.id.occupied_seat_finish_time_tv);
        report_btn = findViewById(R.id.occupied_seat_report_btn);

        seat_name = getIntent().getExtras().getString("seat_name");
        seat_name_tv.setText(seat_name);

        seat_id = getIntent().getExtras().getString("seat_id");
        seat_floor_id = getIntent().getExtras().getString("seat_floor_id");

        seat_initial_time = getIntent().getExtras().getInt("seat_initial_time");
        seat_start_time_tv.setText("" + seat_initial_time);

        seat_finish_time = getIntent().getExtras().getInt("seat_finish_time");
        seat_finish_time_tv.setText("" + seat_finish_time);

        seat_user = getIntent().getExtras().getString("seat_user");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Pisos").child(seat_floor_id).child("Seats").child(seat_id);
        user_reference = database.getReference("Usuarios").child(seat_user);

        report_list = findViewById(R.id.occupied_seat_report_list);
        reports = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, reports);
        report_list.setAdapter(arrayAdapter);

        DatabaseReference report_reference = reference.child("Reports");
        report_reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String report = dataSnapshot.getValue(String.class);
                reports.add(report);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String report = dataSnapshot.getValue(String.class);
                reports.remove(report);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToReportOccupiedSeat();
            }
        });
    }

    public void goToReportOccupiedSeat() {
        Intent intent = new Intent(this, ReportOccupiedSeat.class);
        intent.putExtra("seat_name", seat_name);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(requestCode == RESULT_OK) {
                String loud = data.getExtras().getString("loud");
                if(loud.equals("-") ) {
                    loud = null;
                }

                String damage = data.getExtras().getString("damage");
                if(damage.equals("-") ) {
                    damage = null;
                }

                String bad_use = data.getExtras().getString("bad_use");
                if(bad_use.equals("-") ) {
                    bad_use = null;
                }

                String other = data.getExtras().getString("other");
                if(other.equals("-") ) {
                    other = null;
                }

                /*
                DatabaseReference report_reference1 = reference.child("Reports").push();
                report_reference1.setValue(scratched);

                DatabaseReference report_reference2 = reference.child("Reports").push();
                report_reference2.setValue(connection_failure);

                DatabaseReference report_reference3 = reference.child("Reports").push();
                report_reference3.setValue(time_indicator_failure);

                DatabaseReference report_reference4 = reference.child("Reports").push();
                report_reference4.setValue(other);
                */

                DatabaseReference user_reference1 = user_reference.child("Reports").push();
                user_reference1.setValue(loud);

                DatabaseReference user_reference2 = user_reference.child("Reports").push();
                user_reference2.setValue(damage);

                DatabaseReference user_reference3 = user_reference.child("Reports").push();
                user_reference3.setValue(bad_use);

                DatabaseReference user_reference4 = user_reference.child("Reports").push();
                user_reference4.setValue(other);
            }

            else if(requestCode == RESULT_CANCELED) {
                Log.e("Data", "Error no tan normalito");
            }
        }
    }
}
