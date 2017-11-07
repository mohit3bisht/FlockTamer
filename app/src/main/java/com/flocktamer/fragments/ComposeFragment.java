package com.flocktamer.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flocktamer.Flock;
import com.flocktamer.R;
import com.flocktamer.http.RetrofitBuilder;
import com.flocktamer.http.RetrofitService;
import com.flocktamer.model.BaseModel;
import com.flocktamer.utils.Constants;
import com.flocktamer.utils.FlockTamerLogger;
import com.flocktamer.utils.Util;
import com.millennialmedia.InlineAd;
import com.millennialmedia.MMException;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.millennialmedia.internal.utils.ThreadUtils.runOnUiThread;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeFragment extends BaseFragment implements View.OnClickListener {


    /**
     * Instantiates a new Compose fragment.
     */
    public ComposeFragment() {
        // Required empty public constructor
    }

    private ImageView mPostImage;
    private Button mSubmit;
    private int REQ_GALLERY = 1, REQ_CAMERA = 2;
    private String imagePath = "";
    /**
     * The Imagepath.
     */
    static String IMAGEPATH;
    /**
     * The Permission request code.
     */
    final int PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 101;
    private static final int GALLERY_PIC_REQUEST = 102;
    private static String mTwitterId, userIdd;
    private EditText mTweetText;
    private TextView mTextCounter;
    private RelativeLayout mParentLayout;
    private String mCurrentPhotoPath;
    private static final String FILE_PROVIDER = "com.flocktamer.fileprovider";
    private InlineAd inlineAd;
    public static final String PLACEMENT_ID = "238925";
   // private ADFView adFalconView;

    /**
     * New instance compose fragment.
     *
     * @param activity  the activity
     * @param twitterId the twitter id
     * @param userId    the user id
     * @return the compose fragment
     */
    public static ComposeFragment newInstance(Activity activity, String twitterId, String userId) {
        ComposeFragment fragment = new ComposeFragment();
        mTwitterId = twitterId;
        userIdd = userId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose_new, container, false);
        initIDs(view);
        View adContainer = (RelativeLayout)view.findViewById(R.id.ad_container);
        try {
            inlineAd = InlineAd.createInstance(PLACEMENT_ID, (ViewGroup) adContainer);

            inlineAd.setListener(new InlineAd.InlineListener() {
                @Override
                public void onRequestSucceeded(InlineAd inlineAd) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                          //  adContainer.setVisibility(View.VISIBLE);

                            /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                            toolbar.getMenu().findItem(R.id.inline_refresh).setVisible(true);*/
                        }
                    });

                    FlockTamerLogger.createLog("Inline Banner Ad loaded.");
                }


                @Override
                public void onRequestFailed(InlineAd inlineAd, InlineAd.InlineErrorStatus errorStatus) {

                    FlockTamerLogger.createLog("err" + errorStatus.toString());
                }


                @Override
                public void onClicked(InlineAd inlineAd) {

                    FlockTamerLogger.createLog("Inline Banner Ad clicked.");
                }


                @Override
                public void onResize(InlineAd inlineAd, int width, int height) {

                    FlockTamerLogger.createLog("Inline Banner Ad starting resize.");
                }


                @Override
                public void onResized(InlineAd inlineAd, int width, int height, boolean toOriginalSize) {

                    FlockTamerLogger.createLog("Inline Banner Ad resized.");
                }


                @Override
                public void onExpanded(InlineAd inlineAd) {

                    FlockTamerLogger.createLog("Inline Banner Ad expanded.");
                }


                @Override
                public void onCollapsed(InlineAd inlineAd) {

                    FlockTamerLogger.createLog("Inline Banner Ad collapsed.");
                }


                @Override
                public void onAdLeftApplication(InlineAd inlineAd) {

                    FlockTamerLogger.createLog("Inline Banner Ad left application.");
                }
            });

            // uncomment to set a refresh rate of 30 seconds
            // inlineAd.setRefreshInterval(30000);

            // Go ahead and request an ad
          //  requestAd();
        } catch (MMException e) {
            FlockTamerLogger.createLog(" creating inline banner ad" + e);
        }
        mTweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextCounter.setText(mTweetText.getText().toString().length() + "/140");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


     /*   try {
// create new instance of ADFView
            adFalconView = new ADFView(getActivity());
//Ensure test mode is set to false before your app is released
            adFalconView.setTestMode(false);
// initialize the view by passing a site id, ad unit size and enable auto refresh.
// then load first ad
            adFalconView.initialize("f09dadb9101b4e66902d855dc41622da", ADFAdSize.AD_UNIT_320x50, null, null, true);
// Add ADFView to this activity.
// we consider there is a layout naming linearLayout and
// you want to add ADFView in this layout
            ((RelativeLayout) view.findViewById(R.id.ad_container)).addView(adFalconView);
        } catch (Exception ex) {

            FlockTamerLogger.createLog("AdFalcon Error: "+ex.getMessage());
        }
        adFalconView.setListener(new ADFListener() {
            @Override
            public void onLoadAd(ADFAd adfAd) {
                FlockTamerLogger.createLog("AdFalcon 1: ");

            }

            @Override
            public void onError(ADFAd adfAd, ADFErrorCode adfErrorCode, String s) {
                FlockTamerLogger.createLog("AdFalcon 2: "+s);
            }

            @Override
            public void onPresentAdScreen(ADFAd adfAd) {
                FlockTamerLogger.createLog("AdFalcon 3: ");
            }

            @Override
            public void onDismissAdScreen(ADFAd adfAd) {
                FlockTamerLogger.createLog("AdFalcon 4: ");
            }

            @Override
            public void onLeaveApplication() {
                FlockTamerLogger.createLog("AdFalcon 5: ");
            }
        });*/

        return view;
    }

    private void requestAd() {

        if (inlineAd != null) {
            FlockTamerLogger.createLog("InLine !=null");
            //The AdRequest instance is used to pass additional metadata to the server to improve ad selection
            final InlineAd.InlineAdMetadata inlineAdMetadata = new InlineAd.InlineAdMetadata().
                    setAdSize(InlineAd.AdSize.BANNER);

            //Request ads from the server.  If automatic refresh is enabled for your placement new ads will be shown
            //automatically
            inlineAd.request(inlineAdMetadata);
        } else {
            FlockTamerLogger.createLog("InLine==null");
        }
    }

    /**
     * Initializing the views of this fragment
     *
     * @param view
     */
    private void initIDs(View view) {
        FlockTamerLogger.createLog(" Name : " + this.getClass().getCanonicalName());
        mPostImage = (ImageView) view.findViewById(R.id.iv_post_image);
        mTweetText = (EditText) view.findViewById(R.id.et_tweet_text);
        //mTweetText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mTweetText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mTweetText.setImeActionLabel(getResources().getString(R.string.ok), EditorInfo.IME_ACTION_DONE);
        mTweetText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mTextCounter = (TextView) view.findViewById(R.id.tv_post_text_counter);
        mParentLayout = (RelativeLayout) view.findViewById(R.id.parent_view);
        mSubmit = (Button) view.findViewById(R.id.btn_submit);
        mPostImage.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mParentLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.iv_post_image:
                picFuntion();
                break;

            case R.id.btn_submit:
                callSubmitAPI();
                break;
            case R.id.parent_view:
                Util.hideKeyboard(getActivity());
                break;
        }
    }

    /**
     * Pic funtion.
     */
    public void picFuntion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(getString(R.string.gallery),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkPermission()) {
                            //  openGallery();
                            dispatchSelectPictureIntent();
                        } else {
                            requestPermission();
                        }

                    }
                });

        builder.setNegativeButton(getString(R.string.camera),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkPermission()) {

                            FlockTamerLogger.createLog("checkPermission if");
                            dispatchTakePictureIntent();
                            //openCamera();
                            // captureImage();

                        } else {
                            FlockTamerLogger.createLog("checkPermission else");
                            requestPermission();

                        }

                    }
                }).show();


    }

    /**
     * @return true if permission granted else false.
     * @purpsoe: Use to check camera permission
     */
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    /**
     * @prpose: Request for Camera and Read External Storage permission runtime.
     */
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

            dispatchTakePictureIntent();
            //Toast.makeText(getActivity(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    dispatchTakePictureIntent();
                } else {

                }
                break;

        }
    }

    /**
     * create path for image that we take through camera and start native camera.
     */
    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        IMAGEPATH = Environment.getExternalStorageDirectory() + File.separator + getDateName() + "image.jpg";
        imagePath = IMAGEPATH;
        File file = new File(IMAGEPATH);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQ_CAMERA);
        }


    }
/*
    */

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     *//*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) {

            FlockTamerLogger.createLog("image path.... " + IMAGEPATH);
            imagePath = IMAGEPATH;
            File file = new File(IMAGEPATH);

            Bitmap bitmap = Util.decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);


            mPostImage.setBackgroundResource(0);

            mPostImage.setImageBitmap(bitmap);




        } else if (requestCode == REQ_GALLERY && resultCode == RESULT_OK) {

            try {
                // We need to recyle unused bitmaps
                Uri uri = data.getData();
             //  Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", createImageFile());
                imagePath = Util.getPath(uri, getActivity());

                FlockTamerLogger.createLog("Image path  : : : : : : :  " + imagePath);
                InputStream stream = getActivity().getContentResolver().openInputStream(
                        data.getData());
                Bitmap bitmap = (Bitmap) BitmapFactory.decodeStream(stream);
                stream.close();


                mPostImage.setBackgroundResource(0);
                //   mProfileImage.setImageBitmap(null);
                mPostImage.setImageBitmap(bitmap);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            if (mCurrentPhotoPath != null) {
                File file = new File(mCurrentPhotoPath);
                Bitmap bitmap = Util.decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);


                mPostImage.setBackgroundResource(0);

                mPostImage.setImageBitmap(bitmap);
            }

        } else if (requestCode == GALLERY_PIC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            FlockTamerLogger.createLog("URI>>>>>>>> " + uri);

            mCurrentPhotoPath = Util.getPath(uri, getActivity());

            if (mCurrentPhotoPath != null) {
                InputStream stream = null;
                try {
                    stream = getActivity().getContentResolver().openInputStream(
                            data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = (Bitmap) BitmapFactory.decodeStream(stream);
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                mPostImage.setBackgroundResource(0);
                //   mProfileImage.setImageBitmap(null);
                mPostImage.setImageBitmap(bitmap);

            }
        }
    }

    /**
     * take date/time string to make unique file.
     *
     * @return: Date String
     */
    private String getDateName() {
        Date date = new Date();
        String format = "yyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        String dateString = sdf.format(date);

        return dateString;
    }

    private void openGallery() {
        Intent intent;
        if (Environment
                .getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED)) {

            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQ_GALLERY);
    }

    private void callSubmitAPI() {
        final HashMap<String, Object> data = new HashMap<>();

        data.put("tweet_text", mTweetText.getText().toString());
        data.put("twitter_user_id", Long.parseLong(mTwitterId));
        data.put("user_id", Long.parseLong(userIdd));
        Call<BaseModel> call = null;
        Dialog dialogLocal = null;

        dialogLocal = showProgress();
        RetrofitService service = RetrofitBuilder.getService();

        if (mCurrentPhotoPath.length() > 0) {

            File imageFile = new File(mCurrentPhotoPath);
            FlockTamerLogger.createLog("Image Path final:::: " + mCurrentPhotoPath.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);

            MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("media_image", imageFile.getName(), requestBody);
            FlockTamerLogger.createLog("Auth Header: " + getAppPref().getAccessToken());
            call = service.uploadPostImage(getAppPref().getAccessToken(), data, imageFileBody);
        } else {
            call = service.uploadPostImage(getAppPref().getAccessToken(), data, null);
        }
        final Dialog finalDialogLocal = dialogLocal;
        call.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response.code() == 200) {

                    try {
                        if (finalDialogLocal != null && finalDialogLocal.isShowing()) {
                            finalDialogLocal.dismiss();
                        }
                        FlockTamerLogger.createLog("Params: " + data.toString());
                        Toast.makeText(getActivity(), "Message: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if (response.body().getStatus().equals("success")) {

                            getActivity().finish();
                        } else {

                        }
/*
                            if (response.body().getStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS)) {

                            }
                            else {

                            }*/
                        FlockTamerLogger.createLog("response: " + response.body().getMessage());
                        // listener.onWebServiceComplete(HttpServiceResponse.getResponse(type, response.body().string()), type);
                    } catch (Exception e) {
                        if (finalDialogLocal != null && finalDialogLocal.isShowing()) {
                            finalDialogLocal.dismiss();
                        }

                        FlockTamerLogger.createLog("ErrorException : " + e.getMessage());
                        e.printStackTrace();
                    }/* catch (IOException e) {
                            e.printStackTrace();
                        }*/ /*catch (WebStatusException e) {
                            listener.onWebServiceFailure(e.getBean(), type);
                        }*/

                } else if (response.code() == 401) {
                    //TODO logout app.
                    FlockTamerLogger.createLog("ErrorException :11 " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                if (finalDialogLocal != null && finalDialogLocal.isShowing()) {
                    finalDialogLocal.dismiss();
                }
            }
        });

    }


    @Override
    protected void initViews(View view) {

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        FILE_PROVIDER,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }


    /**
     * dispatch intent to select image from Gallery
     */
    private void dispatchSelectPictureIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType(Constants.IMAGE_TYPE);
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, getString(R.string.gallery));
        startActivityForResult(chooser, GALLERY_PIC_REQUEST);
    }


    /**
     * @return file created using camera request
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
