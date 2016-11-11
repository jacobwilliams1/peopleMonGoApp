package com.example.rodneytressler.budge.Stages;

import android.app.Application;

import com.davidstemmer.screenplay.stage.Stage;
import com.example.rodneytressler.budge.PeopleMonGo;
import com.example.rodneytressler.budge.Riggers.VerticalSlideRigger;
import com.example.rodneytressler.budget.R;

/**
 * Created by jacobwilliams on 11/7/16.
 */

public class RegisterStage extends IndexedStage {
    private final VerticalSlideRigger rigger;

    public RegisterStage(Application context){
        super(MapStage.class.getName());
        // this is calling the Riggers/SlideRigger
        this.rigger = new VerticalSlideRigger(context);
    }

    public RegisterStage() {
        this(PeopleMonGo.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.register_view;
    }

    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}