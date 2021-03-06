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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReservedSeat extends AppCompatActivity {
    TextView seat_name_tv, seat_start_time_tv, seat_finish_time_tv;
    Button report_btn;

    String seat_name, seat_id, seat_floor_id;
    int seat_initial_time, seat_finish_time;

    FirebaseDatabase database;
    DatabaseReference reference;

    ListView report_list;
    ArrayList<String> reports;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserved_seat);

        seat_name_tv = findViewById(R.id.reserved_seat_name_tv);
        seat_start_time_tv = findViewById(R.id.reserved_seat_start_time_tv);
        seat_finish_time_tv = findViewById(R.id.reserved_seat_finish_time_tv);
        report_btn = findViewById(R.id.reserved_seat_report_btn);

        seat_name = getIntent().getExtras().getString("seat_name");
        seat_name_tv.setText(seat_name);

        seat_id = getIntent().getExtras().getString("seat_id");
        seat_floor_id = getIntent().getExtras().getString("seat_floor_id");

        seat_initial_time = getIntent().getExtras().getInt("seat_initial_time");
        seat_start_time_tv.setText("" + seat_initial_time);

        seat_finish_time = getIntent().getExtras().getInt("seat_finish_time");
        seat_finish_time_tv.setText("" + seat_finish_time);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Pisos").child(seat_floor_id).child("Seats").child(seat_id);

        report_list = findViewById(R.id.reserved_seat_report_list);
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
                goToReportReservedSeat();
            }
        });
    }

    public void goToReportReservedSeat() {
        Intent intent = new Intent(this, ReportReservedSeat.class);
        intent.putExtra("seat_name", seat_name);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {

                String scratched = data.getExtras().getString("scratched");
                if(scratched.equals("-") ) {
                    scratched = null;
                }

                String connection_failure = data.getExtras().getString("connection_failure");
                if(connection_failure.equals("-") ) {
                    connection_failure = null;
                }

                String time_indicator_failure = data.getExtras().getString("time_indicator_failure");
                if(time_indicator_failure.equals("-") ) {
                    time_indicator_failure = null;
                }

                String other = data.getExtras().getString("other");
                if(other.equals("-") ) {
                    other = null;
                }

                DatabaseReference report_reference1 = reference.child("Reports").push();
                report_reference1.setValue(scratched);

                DatabaseReference report_reference2 = reference.child("Reports").push();
                report_reference2.setValue(connection_failure);

                DatabaseReference report_reference3 = reference.child("Reports").push();
                report_reference3.setValue(time_indicator_failure);

                DatabaseReference report_reference4 = reference.child("Reports").push();
                report_reference4.setValue(other);
            }

            else if(resultCode == RESULT_CANCELED) {
                Log.e("DATA", "Error para nada normalito");
            }
        }
    }
}
