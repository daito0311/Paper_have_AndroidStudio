package edu.icesi.dmi.paperhive;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import edu.icesi.dmi.paperhive.Mserial.QRlecture;
import edu.icesi.dmi.paperhive.Mserial.mensajeSerial;

public class ReserveSeat extends AppCompatActivity {
    TextView seat_name_tv;
    EditText initial_time_et, finish_time_et;
    Button reserve_btn;

    String seat_id, seat_floor_id;

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_seat);

        seat_name_tv = findViewById(R.id.reserve_seat_name_tv);
        initial_time_et = findViewById(R.id.reserve_seat_initial_time_et);
        finish_time_et = findViewById(R.id.reserve_seat_finish_time_et);
        reserve_btn = findViewById(R.id.reserve_seat_reserve_btn);

        seat_id = getIntent().getExtras().getString("seat_id");
        seat_floor_id = getIntent().getExtras().getString("seat_floor_id");

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = database.getReference("Pisos").child(seat_floor_id).child("Seats").child(seat_id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Seat seat = dataSnapshot.getValue(Seat.class);
                seat_name_tv.setText(seat.getName() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DATA", "Error normalito");
            }
        });



        reserve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveSeat();
            }
        });
    }

    public void reserveSeat() {
        if(initial_time_et.getText().toString().isEmpty() ) {
            Toast.makeText(this, "Debes ingresar la hora de inicio", Toast.LENGTH_LONG).show();
            return;
        }

        if(finish_time_et.getText().toString().isEmpty() ) {
            Toast.makeText(this, "Debes ingresar la hora de finalización", Toast.LENGTH_LONG).show();
            return;
        }



        DatabaseReference user_reference = reference.child("user");
        user_reference.setValue(auth.getCurrentUser().getUid() );

        DatabaseReference occupied_reference = reference.child("occupied");
        occupied_reference.setValue(true);

        DatabaseReference initial_time_reference = reference.child("initialTime");
        final int initial_time_int = Integer.parseInt( initial_time_et.getText().toString() );
        initial_time_reference.setValue(initial_time_int);

        DatabaseReference finish_time_reference = reference.child("finishTime");
        int finish_time_int = Integer.parseInt( finish_time_et.getText().toString() );
        finish_time_reference.setValue(finish_time_int);


        Intent intent = new Intent(this, QRlecture.class);
        intent.putExtra("initial_time", initial_time_int);
        intent.putExtra("finish_time", finish_time_int);
        startActivity(intent);
    }
}
