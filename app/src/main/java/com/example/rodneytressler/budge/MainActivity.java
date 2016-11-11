package com.example.rodneytressler.budge;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.davidstemmer.flow.plugin.screenplay.ScreenplayDispatcher;
import com.example.rodneytressler.budge.Models.ImageLoadedEvent;
import com.example.rodneytressler.budge.Models.User;
import com.example.rodneytressler.budge.Network.RestClient;
import com.example.rodneytressler.budge.Network.UserStore;
import com.example.rodneytressler.budge.Stages.LoginStage;
import com.example.rodneytressler.budge.Stages.MapStage;
import com.example.rodneytressler.budget.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    // flow holds the data of what is where!
    private Flow flow;
    private ScreenplayDispatcher dispatcher;
    public Bundle savedInstanceState;
    private static int RESULT_LOAD_IMAGE = 1;
    private String avatar;


    //butterknife and the convention is two lines no semi-colin.
    @Bind(R.id.container)
    RelativeLayout container;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // we have to have this line. It won't call the container and it'll be empty!
        ButterKnife.bind(this);

        //we get a refence for our main application
        flow = PeopleMonGo.getMainFlow();
        //controls the UI, says this is the main activity and the container is the view.
        dispatcher = new ScreenplayDispatcher(this, container);
        // we set up based on the flow.
        dispatcher.setUp(flow);

        //  testCalls();

        // this can log people out!
        // UserStore.getInstance().setToken(null);

        if (UserStore.getInstance().getToken() == null || UserStore.getInstance().getTokenExpiration() == null) {
            History newHistory = History.single(new LoginStage());
            flow.setHistory(newHistory, Flow.Direction.REPLACE);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (!(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // if it can't do it we need to do some things.
        if (!flow.goBack()) {
            // we get rid of the dispatcher
            flow.removeDispatcher(dispatcher);
            // we delete the history.
            flow.setHistory(History.single(new MapStage()),
                    Flow.Direction.BACKWARD);
            // then try again.
            super.onBackPressed();
        }
    }

    public void getImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{

            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imageString = cursor.getString(columnIndex);
                cursor.close();

//                //Convert to Bitmap Array
//                Bitmap bm = BitmapFactory.decodeFile(imageString);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//                byte[] b = baos.toByteArray();
//
//                //Take the bitmap Array and e
//                // encode it to Base64
//                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//
//                Log.d("***BASE64****", encodedImage);
//                updateInfo(encodedImage);

                //Make API Call to Send Base64 to Server
                EventBus.getDefault().post(new ImageLoadedEvent(imageString));
            }else{
                Toast.makeText(this,"Error Retrieving Image", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e){
            Toast.makeText(this,"Error Retrieving Image", Toast.LENGTH_LONG).show();
        }
    }
    public void updateInfo(String imageString) {
        avatar = imageString;
        final User changeAvatar = new User(null, avatar);
        RestClient restClient = new RestClient();
        restClient.getApiService().updateInfo(changeAvatar).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Image Successfully added", Toast.LENGTH_SHORT).show();

                    changeAvatar.setFullName(avatar);
                    Flow flow = PeopleMonGo.getMainFlow();
                    History newHistory = History.single(new MapStage());
                    flow.setHistory(newHistory, Flow.Direction.BACKWARD);
                }else{
                    Toast.makeText(MainActivity.this, "Information Update Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_LONG).show();
            }
        });
    }

}
