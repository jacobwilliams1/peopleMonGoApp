package com.example.rodneytressler.budge.Stages;

import android.app.Application;

import com.davidstemmer.screenplay.stage.Stage;
import com.example.rodneytressler.budge.PeopleMonGo;
import com.example.rodneytressler.budge.Riggers.VerticalSlideRigger;
import com.example.rodneytressler.budget.R;

/**
 * Created by jacobwilliams on 11/7/16.
 */

public class LoginStage extends IndexedStage {
    private final VerticalSlideRigger rigger;

    public LoginStage(Application context){
        super(MapStage.class.getName());
        // this is calling the Riggers/SlideRigger
        this.rigger = new VerticalSlideRigger(context);
    }

    public LoginStage() {
        this(PeopleMonGo.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_view;
    }

    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}