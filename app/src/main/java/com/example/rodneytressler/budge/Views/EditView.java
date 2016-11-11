package com.example.rodneytressler.budge.Views;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rodneytressler.budge.MainActivity;
import com.example.rodneytressler.budge.Models.DrawStartEvent;
import com.example.rodneytressler.budge.PeopleMonGo;
import com.example.rodneytressler.budge.Stages.EditStage;
import com.example.rodneytressler.budge.Stages.MapStage;
import com.example.rodneytressler.budget.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static com.example.rodneytressler.budge.Components.Utils.encodeTobase64;

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
    public EditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    @OnClick(R.id.save_button)
    public void btnClick(){
        Flow flow = PeopleMonGo.getMainFlow();
        flow.goBack();
        Toast.makeText(context,"User name Saved", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }
    @OnClick(R.id.imageButton)
    public void setImgBtn(){
        ((MainActivity)context).getImage();

        EventBus.getDefault().post(new DrawStartEvent());
    }

}
