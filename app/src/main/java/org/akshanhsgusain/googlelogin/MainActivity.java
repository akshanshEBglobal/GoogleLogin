package org.akshanhsgusain.googlelogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
   private ImageView mProfilePicture;
   private TextView mProfileInfo;
   private Button mLogoutButton;
   private SignInButton mSignInButton;
   private GoogleApiClient mGoogleApiClient;
   private static final int REQ_CODE = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProfilePicture=findViewById(R.id.profileImage_ImageView);
        mProfileInfo=findViewById(R.id.profileInfo_TextView);
        mLogoutButton=findViewById(R.id.logout_Button);
        mSignInButton=findViewById(R.id.google_sign_Button);
        mSignInButton.setOnClickListener(this);
        mLogoutButton.setOnClickListener(this);
        //Build a sign in option
        GoogleSignInOptions mSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        //buildgoogleAPI Client
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(MainActivity.this,this)
                                        .addApi(Auth.GOOGLE_SIGN_IN_API,mSignInOptions).build();
    }

    @Override
    public void onClick(View v) {
          switch(v.getId()){
              case R.id.google_sign_Button: signIn();
                                                               break;
              case R.id.logout_Button : logout();
                                                       break;

          }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn(){
        Intent intent= Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQ_CODE);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode==9001)
         {
                GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleResult(result);
         }
         else{
             Toast.makeText(this, "Result Code did not match", Toast.LENGTH_SHORT).show();
         }
    }

    private void logout(){

        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mProfilePicture.setImageResource(R.mipmap.ic_launcher);
        mProfileInfo.setText("");

       }
    private void handleResult(GoogleSignInResult result){
              if(result.isSuccess()){
                  GoogleSignInAccount account= result.getSignInAccount();
                  String data = account.getDisplayName();
                  data= data +"\n"+ account.getEmail();
                  try {
                      String profileImageURL = account.getPhotoUrl().toString();
                      Glide.with(this).load(profileImageURL).into(mProfilePicture);
                  }
                  catch (Exception e){

                  }

                  mProfileInfo.setText(data);
              }
              else{
                  Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show();
              }
    }


}
