package com.example.rodneytressler.budge.Stages;

import com.davidstemmer.screenplay.stage.XmlStage;

/**
 * Created by rodneytressler on 10/31/16.
 */

//this will reference a specific View with a String id.
public abstract class IndexedStage extends XmlStage{
    public final String id;

    protected IndexedStage(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexedStage that = (IndexedStage) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
