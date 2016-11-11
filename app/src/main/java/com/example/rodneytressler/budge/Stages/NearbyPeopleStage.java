package com.example.rodneytressler.budge.Stages;

import android.app.Application;

import com.example.rodneytressler.budge.PeopleMonGo;
import com.example.rodneytressler.budge.Riggers.SlideRigger;
import com.example.rodneytressler.budget.R;

/**
 * Created by jacobwilliams on 11/11/16.
 */

public class NearbyPeopleStage extends IndexedStage {
    private final SlideRigger rigger;

    public NearbyPeopleStage(Application context) {
        super(MapStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public NearbyPeopleStage() {
        this(PeopleMonGo.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.nearby_user_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}