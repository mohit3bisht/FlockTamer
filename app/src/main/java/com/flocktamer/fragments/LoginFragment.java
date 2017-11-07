package com.flocktamer.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.flocktamer.R;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.model.UserModel;
import com.flocktamer.ui.DigitActivity;
import com.flocktamer.ui.SignupActivity;
import com.flocktamer.ui.TabsInitActivity;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends BaseFragment {

  /*  @BindView(R.id.auth_btn_digit)
    DigitsAuthButton btnDigit;*/
  @BindView(R.id.auth_btn_digit)
  Button btnDigit;
    @BindView(R.id.btn_login_twitter)
    TwitterLoginButton mTwitterBtn;
    private CallbackManager mCallbackManager;

    public LoginFragment() {
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getBaseActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        twitterBtnInit();
        return view;
    }

    @Override
    protected void initViews(View view) {

    /*    btnDigit.setText("");
        btnDigit.setBackgroundResource(R.drawable.signin_mobile);
        btnDigit.setAuthTheme(R.style.CustomDigitsTheme);
*/
        mTwitterBtn.setText("");
        mTwitterBtn.setBackgroundResource(R.drawable.twitter_login_btn);
        mTwitterBtn.setCompoundDrawables(null, null, null, null);
        //callDigitLogin();
    }

    @OnClick(R.id.btn_login_email)
    public void onEmailLogin() {
        Intent intent = new Intent(getBaseActivity(), SignupActivity.class);
        intent.putExtra(Constants.INTENT_SIGNUP_FRAGMENT, Constants.LOGIN_FRAGMENT);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login_signup)
    public void onSignUpClick() {
        getBaseActivity().start(SignupActivity.class);
    }

       @OnClick(R.id.auth_btn_digit)

    void callDigitLogin() {
           startActivityForResult(
                   AuthUI.getInstance()
                           .createSignInIntentBuilder()
                           .setAvailableProviders(Arrays.asList(
                                   new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
           //new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()
                           ))
                           .build(),
                   RC_SIGN_IN);



        // Digits.clearActiveSession();
          // OnVerificationStateChangedCallbacks


       /* startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                // Other providers you want to support
                                // ...
                        ))
                        .build(),
                121);
//
        AuthCallback callback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {

                HashMap<String, String> data = new HashMap<>();

                try {

                    if (session.getAuthToken() instanceof TwitterAuthToken) {
                        final TwitterAuthToken authToken = session.getAuthToken();
                        data.put("digit_access_token", authToken.token);
                        data.put("digit_secret", authToken.secret);
                    }
                    data.put("digit_id", session.getId() + "");
                    data.put("phone", phoneNumber);
                    if (session.isValidUser()) {
                        data.put("digit_is_verified", 1 + "");
                    } else {
                        data.put("digit_is_verified", 0 + "");
                    }
                    data.put("device_type", "android");
                    data.put("device_token", FirebaseInstanceId.getInstance().getToken());

                    callDigitLoginAPI(data);
                } catch (Exception e) {
                    Util.showErrorSnackBar(getView(), "something wrong with Digit");
                }
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        };*/

      //  btnDigit.setCallback(callback);

    }

    private void callDigitLoginAPI(final HashMap<String, String> data) {

        Call<UserModel> service = RetrofitBuilder.getService().digitLogin(data);

        service.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                        getAppPref().setUserLoginDetails(response.body().getUser());
                        getBaseActivity().start(TabsInitActivity.class);

                    } else {
                        Intent intent = new Intent(getBaseActivity(), DigitActivity.class);
                        intent.putExtra(Constants.DIGIT_FRAGMENT_KEY, Constants.KEY_DIGIT_FRAGMENT);
                        intent.putExtra(Constants.DIGIT_INFO, data);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });

    }


    @OnClick(R.id.btn_login_fb)
    void onFbLogin() {
        LoginManager fbLoginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        fbLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // here write code When Login successfully
//                callGraphRequestFb();

                callGraphRequestFb();
//                HashMap<String, String> data = new HashMap<>();
//                data.put("fb_id", loginResult.getAccessToken().getUserId());
//                data.put("device_token", FirebaseInstanceId.getInstance().getToken());
//                data.put("device_type", "android");
//                fbLoginApi(data);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                // here write code when get error
                Log.e("FAcebook", e.getMessage());
                Util.showErrorSnackBar(getView(), e.getMessage());

            }
        });
        fbLoginManager.logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

    }
    private static final int RC_SIGN_IN = 123;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            final IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {

                FlockTamerLogger.createLog("response1: "+response.getIdpToken());
                final FirebaseAuth auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null)
                    auth.getCurrentUser().getIdToken(true)
                            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        final String idToken = task.getResult().getToken();
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        FlockTamerLogger.createLog("responsdddde1: "+auth.getCurrentUser().getUid());// userid
                                        FlockTamerLogger.createLog("responscccccdddde1: "+idToken); //secret, token

                                        hashMap.put("digit_access_token", idToken);
                                        hashMap.put("digit_secret", idToken);
                                        hashMap.put("digit_id", auth.getCurrentUser().getUid());
                                        hashMap.put("phone", response.getPhoneNumber());
                                        hashMap.put("digit_is_verified", 1 + "");
                                        hashMap.put("device_type", "android");
                                        hashMap.put("device_token", FirebaseInstanceId.getInstance().getToken());
                                        callDigitLoginAPI(hashMap);
                                    }

                                }
                            });

                if (data.getExtras()!=null){
                    FlockTamerLogger.createLog("Dataaaa: "+data.getExtras().keySet().toString());

                }
                FlockTamerLogger.createLog("response2: "+response.getIdpSecret());
                FlockTamerLogger.createLog("response3: "+response.getPhoneNumber());
                FlockTamerLogger.createLog("response4: "+response.getProviderType());
               /* startActivity(SignedInActivity.createIntent(this, response));
                finish();*/
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                   // showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                 //   showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                  //  showSnackbar(R.string.unknown_error);
                    return;
                }
            }

         //   showSnackbar(R.string.unknown_sign_in_response);
        }
      else  if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            if (mCallbackManager != null) {
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
            }

        } else {


            mTwitterBtn.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void callGraphRequestFb() {
//        final HashMap<String, String> data = new HashMap<>();
//        data.put("device_token", FirebaseInstanceId.getInstance().getToken());
//        data.put("device_type", "android");
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        try {
                            if (object != null) {
                                String email = "";
                                HashMap<String, String> data = new HashMap<>();
                                if (object.has("email")) {
                                    email = object.getString("email");
                                    data.put("email", email);
                                }

                                data.put("fb_id", object.getString("id"));
//                                data.put("phone", "");
                                data.put("first_name", object.getString("first_name"));
                                data.put("last_name", object.getString("last_name"));
//
                                data.put("device_type", "android");
                                data.put("device_token", FirebaseInstanceId.getInstance().getToken());

                                fbLoginApi(data,object);
//                                Intent intent = new Intent(getBaseActivity(), DigitActivity.class);
//                                intent.putExtra(Constants.DIGIT_FRAGMENT_KEY, Constants.KEY_FACEBOOK_FRAGMENT);
//                                intent.putExtra("id", object.getString("id"));
//                                intent.putExtra("fname", object.getString("first_name"));
//                                intent.putExtra("lname",  object.getString("first_name"));
//                                intent.putExtra("email", email);
//                                intent.putExtra("phone_")

//                                startActivity(intent);
//                                replaceFragment(FacebookDetailFragment.newInstance(object.getString("id"), object.getString("first_name"), object.getString("last_name"), email));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {

//                            Utils.showToast(mActivity,"Problem in fetching data from facebook.");
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void fbLoginApi(HashMap<String, String> data, final JSONObject object) {

        RetrofitBuilder.getService().facebookRegister(data).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {
                        getAppPref().setUserLoginDetails(response.body().getUser());
                        getBaseActivity().start(TabsInitActivity.class);

                    } else {

                        Intent intent = new Intent(getBaseActivity(), DigitActivity.class);

                                intent.putExtra(Constants.DIGIT_FRAGMENT_KEY, Constants.KEY_FACEBOOK_FRAGMENT);
                        try {
                            String email = "";
                            if (object.has("email")) {
                                email = object.getString("email");
                            }
                            intent.putExtra("id", object.getString("id"));
                            intent.putExtra("fname", object.getString("first_name"));
                            intent.putExtra("lname",  object.getString("last_name"));
                            intent.putExtra("email", email);

                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        Util.showErrorSnackBar(getView(), response.message());
//                        callGraphRequestFb();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });

    }


    private void twitterBtnInit() {
        mTwitterBtn.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                final TwitterSession session = result.data;

                Twitter.getApiClient(session).getAccountService()
                        .verifyCredentials(false, false).enqueue(new com.twitter.sdk.android.core.Callback<User>() {

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void success(Result<User> userResult) {

                        User user = userResult.data;
                        HashMap<String, String> data = new HashMap<>();
                        data.put("twitter_id", user.getId() + "");
                        data.put("device_type", "android");
                        data.put("device_token", FirebaseInstanceId.getInstance().getToken());

                        twitterApi(data, user, session);

//                        data.put("twitter_oauth_token_secret", session.getAuthToken().secret);
//                        data.put("twitter_screen_name", user.screenName);
//                        data.put("twitter_name", user.name);
//
//                        data.put("twitter_profile_image_url", user.profileImageUrl);
//                        data.put("twitter_profile_image_url_https", user.profileImageUrlHttps);
////
//                        ["", "device_type", "twitter_id"]

                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    private void twitterApi(final HashMap<String, String> data, final User user, final TwitterSession session) {

        Call<UserModel> call = RetrofitBuilder.getService().twitterLogin(data);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressDialog.dismiss();

                if (response.code() == 200) {
                    if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {

                        if (response.body().getUser().getStatus().trim().equalsIgnoreCase("1")) {
                            getAppPref().setUserLoginDetails(response.body().getUser());
                            getBaseActivity().start(TabsInitActivity.class);
                        } else {
                            Intent intent = new Intent(getBaseActivity(), SignupActivity.class);
                            intent.putExtra(Constants.INTENT_SIGNUP_FRAGMENT, Constants.CONFIRM_FRAGMENT);
                            intent.putExtra(Constants.INTENT_USER_ID, response.body().getUser().getId());
                            intent.putExtra(Constants.INTENT_MOBILE, response.body().getUser().getPhone());

                            startActivity(intent);
//                            replaceFragment(ConfirmPhoneFragment.newInstance(response.body().getUser().getId()));
                        }
//                        getAppPref().setUserLoginDetails(response.body().getUser());
//                        getBaseActivity().start(TabsInitActivity.class);

                    } else {
                        Intent intent = new Intent(getBaseActivity(), DigitActivity.class);
                        intent.putExtra(Constants.DIGIT_FRAGMENT_KEY, Constants.KEY_TWITTER_FRAGMENT);

                        String fname = "", lname = "";
                        String[] name = user.name.split("\\s+");
                        if (name.length == 1) {
                            fname = name[0];
                        } else if (name.length == 2) {
                            fname = name[0];
                            lname = name[1];
                        }


                        HashMap<String, String> data = new HashMap<>();

                        data.put("twitter_x_auth_expires", "");

                        data.put("first_name", fname);
                        data.put("last_name", lname);
                        data.put("device_token", FirebaseInstanceId.getInstance().getToken());
                        data.put("device_type", "android");
                        data.put("twitter_id", user.getId() + "");
                        data.put("twitter_oauth_token", session.getAuthToken().token);
                        data.put("twitter_oauth_token", session.getAuthToken().token);
                        data.put("twitter_oauth_token_secret", session.getAuthToken().secret);
                        data.put("twitter_screen_name", user.screenName);
                        data.put("twitter_x_auth_expires", "12346782562323");
                        data.put("twitter_name", user.name);

                        data.put("twitter_profile_image_url", user.profileImageUrl);
                        data.put("twitter_profile_image_url_https", user.profileImageUrlHttps);

                        intent.putExtra(Constants.DIGIT_INFO, data);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }



}
