package com.example.rodneytressler.budge.Views;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.rodneytressler.budge.Components.Utils;
import com.example.rodneytressler.budge.MainActivity;
import com.example.rodneytressler.budge.Models.User;
import com.example.rodneytressler.budge.Network.RestClient;
import com.example.rodneytressler.budge.PeopleMonGo;
import com.example.rodneytressler.budge.Stages.CaughtPeopleListStage;
import com.example.rodneytressler.budge.Stages.EditStage;
import com.example.rodneytressler.budge.Stages.NearbyPeopleStage;
import com.example.rodneytressler.budget.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rodneytressler.budge.PeopleMonGo.getMainFlow;

/**
 * Created by rodneytressler on 10/31/16.
 */

public class PokeMapView extends RelativeLayout implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener {
    private Context context = getContext();
    @Bind(R.id.mapView)
    public MapView mapView;
    @Bind(R.id.floatingActionButton3)
    FloatingActionButton btn;
    @Bind(R.id.floatingActionButton)
    FloatingActionButton btn2;

    public GoogleMap mMap;

    public ArrayList<User> peopleMone;
    public ArrayList<String> caughtPeople;
    private String name;
    private String id;
    private RestClient restClient;


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        restClient = new RestClient();
        mapView.onCreate(((MainActivity) getContext()).savedInstanceState);

        mapView.onResume();

        mapView.getMapAsync(this);
    }

    @OnClick(R.id.floatingActionButton3)
    public void btnClick() {
        Flow flow = getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                //this denotes what we push!
                .push(new EditStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);

    }

    @OnClick(R.id.floatingActionButton)
    public void showAddCategoryView() {
        Flow flow = PeopleMonGo.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new CaughtPeopleListStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }

    @OnClick(R.id.floatingActionButton2)
    public void showNearbyView() {
        Flow flow = PeopleMonGo.getMainFlow();
        History newHistory = flow.getHistory().buildUpon()
                .push(new NearbyPeopleStage())
                .build();
        flow.setHistory(newHistory, Flow.Direction.FORWARD);
    }


    public PokeMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().isMyLocationButtonEnabled();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMarkerClickListener(markerClickListener);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {

        }

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }


    public void checkUser() {
        Integer radius = 100;
        restClient.getApiService().nearby(radius).enqueue(new Callback<User[]>() {
                  @Override
                  public void onResponse(Call<User[]> call, Response<User[]> response) {
                      if (response.isSuccessful()) {
                          peopleMone = new ArrayList<>(Arrays.asList(response.body()));
                          caughtPeople = new ArrayList<>();
                          for (User peopleMonecaught : peopleMone) {
                              name = peopleMonecaught.getUserName();
                              id = peopleMonecaught.getId();
                              LatLng loc = new LatLng(peopleMonecaught.getLatitude(), peopleMonecaught.getLongitude());

                                  try {
                                      Bitmap image = Utils.decodeImage(peopleMonecaught.getAvatarBase64());
                                      mMap.addMarker(new MarkerOptions().title(name).icon(BitmapDescriptorFactory.fromBitmap(image)).snippet(peopleMonecaught.getId()).position(loc));
                                  }catch (Exception e){
                                      mMap.addMarker(new MarkerOptions().position(loc).snippet(peopleMonecaught.getId()).title(name));

                                  }

                              }
                      } else {
                          Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                      }
                  }

                  @Override
                  public void onFailure(Call<User[]> call, Throwable t) {
                      Toast.makeText(context, "No User is near you", Toast.LENGTH_SHORT).show();
                  }
              }
        );

    }


    private GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(context, "You caught " + marker.getTitle(), Toast.LENGTH_SHORT).show();
            String id = marker.getSnippet();
            float radius = 100;
            Double latC = marker.getPosition().latitude;
            Double lngC = marker.getPosition().longitude;
            LatLng markCircle = new LatLng(latC, lngC);
            final Circle circle = mMap.addCircle(new CircleOptions().center(markCircle)
                    .strokeColor(Color.RED).radius(10));
            ValueAnimator vAnimator = new ValueAnimator();
            vAnimator.setRepeatCount(1);
            vAnimator.setRepeatMode(ValueAnimator.REVERSE);  /* PULSE */
            vAnimator.setIntValues(10, 0);
            vAnimator.setDuration(500);
            vAnimator.setEvaluator(new IntEvaluator());
            vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    circle.setRadius(animatedFraction * 10);
                }
            });
            vAnimator.start();
            User userr = new User(id, radius);
            restClient.getApiService().caught(userr).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "SUCCESS!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "User outside of your range", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "FAIL!!!", Toast.LENGTH_SHORT).show();
                }
            });
            marker.remove();
            return true;

        }
    };


    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(loc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18));

            final Circle circle = mMap.addCircle(new CircleOptions().center(loc)
                    .strokeColor(Color.RED).radius(100));

            ValueAnimator valAnim = new ValueAnimator();
            valAnim.setRepeatCount(ValueAnimator.INFINITE);
            valAnim.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
            valAnim.setIntValues(0, 100);
            valAnim.setDuration(5000);
            valAnim.setEvaluator(new IntEvaluator());
            valAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            valAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    circle.setRadius(animatedFraction * 100);
                }
            });
            valAnim.start();


            User user = new User(location.getLatitude(), location.getLongitude());
            RestClient restClient = new RestClient();
            restClient.getApiService().checkIn(user).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        checkUser();
                        Toast.makeText(context, "You checked in", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Check in Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Check in failed", Toast.LENGTH_SHORT).show();

                }
            });
        }
    };
}
