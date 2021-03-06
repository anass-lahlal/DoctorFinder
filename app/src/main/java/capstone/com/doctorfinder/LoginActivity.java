package capstone.com.doctorfinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private AutoCompleteTextView EmailTextView;
    private AutoCompleteTextView PasswordTextView;
    private Button LoginButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EmailTextView =(AutoCompleteTextView) findViewById(R.id.EmailTextView);
        PasswordTextView =(AutoCompleteTextView) findViewById(R.id.PasswordTextView);

        firebaseAuth = firebaseAuth.getInstance();

        LoginButton = (Button) findViewById(R.id.LoginButton);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Patientlogin();
            }
        });
    }

    public void toSignUp(View view) {
        Intent i = new Intent(this,SignUpActivity.class);
        startActivity(i);
    }

    public void Patientlogin()
    {
        String Email = EmailTextView.getText().toString().trim();
        String Password = PasswordTextView.getText().toString().trim();

        if(TextUtils.isEmpty(Email))
        {
            Toast.makeText(this,"please enter E-mail",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            Toast.makeText(this,"E-mail format is incorrect",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(Password))
        {
            Toast.makeText(this,"please enter your Password",Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(LoginActivity.this, "Signing in", Toast.LENGTH_SHORT).show();

        firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {

                    //TODO check if the type of the email and password is a doctor or patient to redirect to the proper page
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("patients").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {

                               String U_ID = firebaseAuth.getCurrentUser().getUid();
                               //makeText(LoginActivity.this,"snapshotkey ="+snapshot.getKey(), Toast.LENGTH_SHORT).show();
                               //Toast.makeText(LoginActivity.this,"U_ID"+U_ID, Toast.LENGTH_SHORT).show();
                                EmailTextView.setText("U_ID"+U_ID);
                                //PasswordTextView.setText();
                                if(snapshot.getKey()== U_ID)
                                {
                                    Intent login = new Intent(LoginActivity.this,user_main.class);
                                    startActivity(login);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {

                        }

                });


                           // Toast.makeText(LoginActivity.this, "done", Toast.LENGTH_SHORT).show();
                    //Intent login = new Intent(LoginActivity.this,user_main.class);
                    //startActivity(login);

                } else {
                    Toast.makeText(LoginActivity.this, "Email and password don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void toMain(View view) {
    }

    @Override
    public void onClick(View v)
    {

        if(v==LoginButton) {

            Patientlogin();
        }
    }
}
