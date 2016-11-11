package com.example.rodneytressler.budge.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rodneytressler.budge.MainActivity;
import com.example.rodneytressler.budge.Models.ImageLoadedEvent;
import com.example.rodneytressler.budge.Models.ImageSized;
import com.example.rodneytressler.budge.Models.User;
import com.example.rodneytressler.budge.Network.RestClient;
import com.example.rodneytressler.budge.PeopleMonGo;
import com.example.rodneytressler.budge.Stages.MapStage;
import com.example.rodneytressler.budget.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jacobwilliams on 11/10/16.
 */

public class EditView extends LinearLayout {
    @Bind(R.id.user_name_choice)
    EditText usernameChange;
    @Bind(R.id.imageButton)
    ImageButton imgBtn;
    @Bind(R.id.save_button)
    Button sveBtn;
    private Context context;
    @Bind(R.id.profile_avatar)
    ImageView profAV;

    private String selectedImage;

    public Bitmap scaledImage;

    public Bitmap originalImage;

    public EditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @OnClick(R.id.save_button)
    public void btnClick() {
        updateInfo();
        Flow flow = PeopleMonGo.getMainFlow();
        flow.goBack();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
//        getInfo();
        updateInfo();
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.imageButton)
    public void setImgBtn() {
        ((MainActivity) context).getImage();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setSelectedImage(ImageLoadedEvent event) {
        selectedImage = event.selectedImage;
        Bitmap image = BitmapFactory.decodeFile(selectedImage);
        profAV.setImageBitmap(image);

    }

//    private void getInfo(){
//        RestClient restClient = new RestClient();
//        restClient.getApiService().userInfo().enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if(response.isSuccessful()) {
//                    User user = response.body();
//                    String encodedImage = user.getAvatarBase64();
//                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
//                    originalImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                    usernameChange.setText(user.getFullName());
//                    profAV.setImageBitmap(originalImage);
//                }else{
//                    Toast.makeText(context, "Load Failed1", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(context, "Load Failed2", Toast.LENGTH_LONG).show();
//
//            }
//        });
//    }


    public void updateInfo() {
        final String name = usernameChange.getText().toString();
        final User changeName = new User(name, null);
        RestClient restClient = new RestClient();
        restClient.getApiService().updateInfo(changeName).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Information Updated", Toast.LENGTH_SHORT).show();
                    changeName.setFullName(name);
                    Flow flow = PeopleMonGo.getMainFlow();
                    History newHistory = History.single(new MapStage());
                    flow.setHistory(newHistory, Flow.Direction.BACKWARD);
                } else {
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failure to update info", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void imageLoaded(ImageLoadedEvent event){
        selectedImage = event.selectedImage;
        int width = profAV.getWidth();
        int height = profAV.getHeight();

        Bitmap image = BitmapFactory.decodeFile(selectedImage);

        float ratio = (float)image.getWidth() / (float)image.getHeight();
        if (ratio > 1){
            height = (int)(width / ratio);
        } else {
            width = (int)(height * ratio);
        }

        scaledImage = Bitmap.createScaledBitmap(image, width, height, true);
        EventBus.getDefault().post(new ImageSized());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void imageReady(ImageSized event){
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(scaledImage);

        //Convert to Bitmap Array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaledImage.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        //Take the bitmap Array and e
        // encode it to Base64
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        makeApiCallForProfile(encodedImage);
    }

    private void makeApiCallForProfile(String imageString){
        String avatar = imageString;
        User user = new User(null, avatar);
        RestClient restClient = new RestClient();
        restClient.getApiService().updateInfo(user).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Is the server response between 200 to 299
                if (response.isSuccessful()){


                }else{
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}
