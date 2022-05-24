package com.kennedy.bmicalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BMIactivity extends AppCompatActivity {

    TextView mbmidisplay, magedisplay, mweightdisplay, mheightdisplay, mbmicategory, mgender;
    Intent intent;

    ImageView mimageview;
    String mbmi;
    String cateogory;
    float intbmi;
    private Button logout;

    String height;
    String weight;

    float intheight, intweight;

    RelativeLayout mbackground;
    android.widget.Button mrecalculatebmi;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    BMIInfo bmiInfo;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiactivity);
        getSupportActionBar().setElevation(0);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1E1D1D"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\"></font>"));
        getSupportActionBar().setTitle("Result");


        intent = getIntent();
        mbmidisplay = findViewById(R.id.bmidisplay);

        mbmicategory = findViewById(R.id.bmicategorydispaly);
        mrecalculatebmi = findViewById(R.id.recalculatebmi);
        mimageview = findViewById(R.id.imageview);
        mgender = findViewById(R.id.genderdisplay);
        mbackground = findViewById(R.id.content);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("BMIInfo");
        logout = findViewById(R.id.signOut);

        bmiInfo = new BMIInfo();


        height = intent.getStringExtra("height");
        weight = intent.getStringExtra("weight");


        intheight = Float.parseFloat(height);
        intweight = Float.parseFloat(weight);

        intheight = intheight / 100;
        intbmi = intweight / (intheight * intheight);


        mbmi = Float.toString(intbmi);
        System.out.println(mbmi);

        if (intbmi < 16) {
            mbmicategory.setText("Severe Thinness");
            mbackground.setBackgroundColor(Color.RED);
            mimageview.setImageResource(R.drawable.crosss);
        } else if (intbmi < 16.9 && intbmi > 16) {
            mbmicategory.setText("Moderate Thinness");
            mbackground.setBackgroundColor(R.color.halfwarn);
            mimageview.setImageResource(R.drawable.warning);
        } else if (intbmi < 18.4 && intbmi > 17) {
            mbmicategory.setText("Mild Thinness");
            mbackground.setBackgroundColor(R.color.halfwarn);
            mimageview.setImageResource(R.drawable.warning);
            //   mimageview.setBackground(colorDrawable2);
        } else if (intbmi < 24.9 && intbmi > 18.5) {
            mbmicategory.setText("Normal");
            mimageview.setImageResource(R.drawable.ok);
        } else if (intbmi < 29.9 && intbmi > 25) {
            mbmicategory.setText("Overweight");
            mbackground.setBackgroundColor(R.color.halfwarn);
            mimageview.setImageResource(R.drawable.warning);
        } else if (intbmi < 34.9 && intbmi > 30) {
            mbmicategory.setText("Obese Class I");
            mbackground.setBackgroundColor(R.color.halfwarn);
            mimageview.setImageResource(R.drawable.warning);
        } else {
            mbmicategory.setText("Obese Class II");
            mbackground.setBackgroundColor(R.color.warn);
            mimageview.setImageResource(R.drawable.crosss);
        }
        mgender.setText(intent.getStringExtra("gender"));
        mbmidisplay.setText(mbmi);


        //getActionBar().hide();
        //mrecalculatebmi=findViewById(R.id.recalculatebmi);

        mrecalculatebmi.setOnClickListener(view -> {
            Intent intent = new Intent(BMIactivity.this, MainActivity.class);
            startActivity(intent);
            finish();



            mrecalculatebmi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // getting text from our edittext fields.
                    String bmidisplay = mbmidisplay.getText().toString();
                    String bmicategorydisplay = mbmicategory.getText().toString();
                    String genderdisplay = "male";//magedisplay.getText().toString();


                    // below line is for checking weather the
                    // edittext fields are empty or not.
                    if (TextUtils.isEmpty(bmidisplay) && TextUtils.isEmpty(bmicategorydisplay) && TextUtils.isEmpty(genderdisplay)) {
                        // if the text fields are empty
                        // then show the below message.
                        Toast.makeText(BMIactivity.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                    } else {
                        // else call the method to add

                        // data to our database.
                        addDatatoFirebase(bmidisplay, bmicategorydisplay, genderdisplay);
                    }
                }


                private void addDatatoFirebase(String mbmidisplay, String mbmicategory, String magedisplay) {

                    bmiInfo.setBmidisplay(mbmidisplay);
                    bmiInfo.setBmicategorydisplay(mbmicategory);
                    bmiInfo.setGenderdisplay(magedisplay);

                    // we are use add value event listener method
                    // which is called with database reference.
                    try {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // inside the method of on Data change we are setting
                                Toast.makeText(BMIactivity.this, "called db ref", Toast.LENGTH_SHORT).show();
                                // our object class to our database reference.
                                // data base reference will sends data to firebase.
                                databaseReference.setValue("test");

                                // after adding this data we are showing toast message.
                                Toast.makeText(BMIactivity.this, "data added", Toast.LENGTH_SHORT).show();
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // if the data is not added or it is cancelled then
                                // we are displaying a failure toast message.
                                Toast.makeText(BMIactivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                            }


                        });
                    }catch (Exception e){
                        Toast.makeText(BMIactivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            });
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(BMIactivity.this,Login.class));
            }
        });

    }
}
