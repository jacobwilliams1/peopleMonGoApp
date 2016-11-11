package com.example.rodneytressler.budge.Stages;

import android.app.Application;

import com.davidstemmer.screenplay.stage.Stage;
import com.example.rodneytressler.budge.PeopleMonGo;
import com.example.rodneytressler.budge.Riggers.VerticalSlideRigger;
import com.example.rodneytressler.budget.R;

/**
 * Created by jacobwilliams on 11/11/16.
 */

public class CaughtPeopleListStage extends IndexedStage {
    private final VerticalSlideRigger rigger;

    public CaughtPeopleListStage(Application context){
        super(MapStage.class.getName());
        // this is calling the Riggers/SlideRigger
        this.rigger = new VerticalSlideRigger(context);
    }

    public CaughtPeopleListStage() {
        this(PeopleMonGo.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.caught_people_list_view;
    }

    @Override
    public Stage.Rigger getRigger() {
        return rigger;
    }
}