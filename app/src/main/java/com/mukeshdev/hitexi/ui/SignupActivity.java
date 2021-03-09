package com.mukeshdev.hitexi.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorResolver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mukeshdev.hitexi.MainActivity;
import com.mukeshdev.hitexi.R;
import com.mukeshdev.hitexi.models.Users;
import com.mukeshdev.hitexi.utility.BaseActivity;

public class SignupActivity extends BaseActivity {

    // Create a VideoView variable, a MediaPlayer variable, and an int to hold the current
    // video position.
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    FirebaseAuth auth; //FirebaseAuth Instance
    Button signIn, register;
    RelativeLayout rootlayout;
    TextInputEditText etemail, etname, etpassword;
    TextInputEditText logemail, logpassword;
    TextInputLayout lyemail, lyname, lypassword;
    LinearLayout progress_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();
        initFindView();
        clickEvents();

        //----------Start for video view-----------------
        // Build your video Uri
        Uri uri = Uri.parse("android.resource://" // First start with this,
                + getPackageName() // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.plane); // and then finally add your video resource. Make sure it is stored
        // in the raw folder.

        // Set the new Uri to our VideoView
        videoBG.setVideoURI(uri);
        // Start the VideoView
        videoBG.start();

        // Set an OnPreparedListener for our VideoView. For more information about VideoViews,
        // check out the Android Docs: https://developer.android.com/reference/android/widget/VideoView.html
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                // We want our video to play over and over so we set looping to true.
                mMediaPlayer.setLooping(true);
                // We then seek to the current posistion if it has been set and play the video.
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });//----------End for video view-----------------
    }

    private void initFindView() {

        videoBG = findViewById(R.id.videoView);
        signIn = findViewById(R.id.signIn);
        register = findViewById(R.id.register);
        rootlayout = (RelativeLayout) findViewById(R.id.rootLaout);
        progress_bar = (LinearLayout) findViewById(R.id.progress_bar);
    }

    private void clickEvents() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerDialogbox();

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInDialogbox();

            }
        });
    }

    private void signInDialogbox() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("LOG IN");
        dialog.setMessage("Please Use Email to Login");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View login_view = layoutInflater.inflate(R.layout.layout_signin, null);

        logemail = login_view.findViewById(R.id.regEmail);
        logpassword = login_view.findViewById(R.id.regPassword);

        dialog.setView(login_view);

        dialog.setPositiveButton("LOG IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();
                progress_bar.setVisibility(View.VISIBLE);

                String Email = logemail.getText().toString();
                final String pass = logpassword.getText().toString();

                //Validation section
                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    progress_bar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    progress_bar.setVisibility(View.GONE);
                    return;
                }
                showProgressBar();
                if (logpassword.length() < 6) {
                    logpassword.setError("Should be greater than 6");
                    progress_bar.setVisibility(View.GONE);
                }

                //authenticate user with email/password by adding complete listener
                auth.signInWithEmailAndPassword(Email, pass)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {@Override public void onComplete(@NonNull Task<AuthResult> task) { if (!task.isSuccessful()) {
                                    // there was an error
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_LONG).show();
                                    Log.e("MyTag", task.getException().toString());
                                     progress_bar.setVisibility(View.GONE);

                                } else {
                                    progress_bar.setVisibility(View.GONE);
                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();
                progress_bar.setVisibility(View.GONE);

            }
        });

        dialog.show();
    }

    private void registerDialogbox() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Register");
        dialog.setMessage("Please Use Email to Register");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View register_view = layoutInflater.inflate(R.layout.layout_register, null);

        etemail = register_view.findViewById(R.id.email);
        etname = register_view.findViewById(R.id.name);
        etpassword = register_view.findViewById(R.id.password);
        lyemail = register_view.findViewById(R.id.lyemail);
        lyname = register_view.findViewById(R.id.lyname);
        lypassword = register_view.findViewById(R.id.lypassword);

        dialog.setView(register_view);

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

//                dialogInterface.dismiss();
                  progress_bar.setVisibility(View.VISIBLE);

                //Fetching data
                String emailInput = etemail.getText().toString().trim();
                String password = etpassword.getText().toString().trim();
                final String user = etname.getText().toString().trim();

                //Validation check
                if (TextUtils.isEmpty(user)) {
                    Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
                    progress_bar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(emailInput)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    progress_bar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    progress_bar.setVisibility(View.GONE);
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    progress_bar.setVisibility(View.GONE);
                    return;
                }


                //create user with email/password by adding complete listener
                auth.createUserWithEmailAndPassword(emailInput, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progress_bar.setVisibility(View.GONE);

                                // If sign-in fails, display a message to the user. If sign-in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_LONG).show();
                                    Log.e("MyTag", task.getException().toString());
                                    progress_bar.setVisibility(View.GONE);
                                } else {
                                    progress_bar.setVisibility(View.GONE);
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();
                progress_bar.setVisibility(View.GONE);

            }
        });

        dialog.show();
    }


    private void nextGo() {
        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = etemail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            lyemail.setError("Required.");
            Snackbar.make(rootlayout, "Email is Required", Snackbar.LENGTH_SHORT)
                    .show();
            valid = false;
        } else {
            lyemail.setError(null);
        }

        String password = etpassword.getText().toString();
        if (password.length() < 6) {
            lypassword.setError("Required.");
            Snackbar.make(rootlayout, "Password not less then 6 characters", Snackbar.LENGTH_SHORT)
                    .show();
            valid = false;
        } else {
            lypassword.setError(null);
        }

        return valid;
    }


    // Checking the current all state
    @Override
    public void onStart() {
        super.onStart();

        progress_bar.setVisibility(View.GONE);
        // if user logged in, go to sign-in screen
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    /*================================ Important Section! ================================
    We must override onPause(), onResume(), and onDestroy() to properly handle our
    VideoView.
     */

    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBG.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        videoBG.start();
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer.release();
        mMediaPlayer = null;
    }*/
}