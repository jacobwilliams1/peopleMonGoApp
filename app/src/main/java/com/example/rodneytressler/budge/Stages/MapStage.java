package com.example.rodneytressler.budge.Stages;

import android.app.Application;

import com.example.rodneytressler.budge.Riggers.SlideRigger;
import com.example.rodneytressler.budge.PeopleMonGo;
import com.example.rodneytressler.budget.R;

/**
 * Created by rodneytressler on 10/31/16.
 */

public class MapStage extends IndexedStage {
    private final SlideRigger rigger;

    public MapStage(Application context) {
        super(MapStage.class.getName());
        this.rigger = new SlideRigger(context);
    }

    public MapStage() {
        this(PeopleMonGo.getInstance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.map_view;
    }

    @Override
    public Rigger getRigger() {
        return rigger;
    }
}


