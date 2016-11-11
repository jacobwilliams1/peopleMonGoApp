package com.example.rodneytressler.budge.Components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by jacobwilliams on 11/10/16.
 */

public class Utils {
    public static Bitmap decodeImage(String encodedString) {
        byte[] decodedString;
        Bitmap decodedByte;
        if (encodedString != null) {
            if (encodedString.contains(",")) {
                String[] haxor = encodedString.split(",");
                decodedString = Base64.decode(haxor[1], Base64.DEFAULT);
            } else {
                if (encodedString.length() > 200) {
                    decodedString = Base64.decode(encodedString, Base64.DEFAULT);
                } else {
                    decodedString = null;
                }
            }
            if (decodedString != null) {
                decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                decodedByte = resize(decodedByte);
                return decodedByte;
            }
        }
        return null;
    }
    public static String encodeTobase64(Bitmap bitmap) {
        bitmap = resize(bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        Constants.IMAGE = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap resize(Bitmap image) {
        image = Bitmap.createScaledBitmap(image, 90, 90, false);
        return image;
    }
}
