package com.example.cetty.derailer.objects;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by reim on 12.02.17.
 */

public class AnimationPath {

    Context mContext;
    final int flags = PathMeasure.POSITION_MATRIX_FLAG |
            PathMeasure.TANGENT_MATRIX_FLAG;

    ArrayList<String> keys = new ArrayList<>();
    HashMap<String,String> pathStrings = new HashMap<>();
    HashMap<String,Path> paths = new HashMap<>();

    public AnimationPath (int connections){
        if (connections == 4){
            pathStrings.put("01","M50,0c0,27.61 22.39,50 50,50");
            pathStrings.put("02","M50,0L50,100");
            pathStrings.put("30","M0,50c27.61,0 50,-22.39 50,-50");
            pathStrings.put("12","M100,50c-27.61,0 -50,22.39 -50,50");
            pathStrings.put("31","M0,50L100,50");
            pathStrings.put("23","M50,100C50,72.39 27.61,50 0,50");
        } else if (connections == 8){
            pathStrings.put("10","m70,0c0,11.05 -8.95,20 -20,20 -11.05,0 -20,-8.95 -20,-20");
            pathStrings.put("02","m30.16,0c0,16.57 13.43,30 30,30L100,30");
            pathStrings.put("30","m100,70c-8.32,0 -15.85,-3.39 -21.29,-8.86l-40,-40C33.33,15.71 30,8.25 30,0");
            pathStrings.put("40","m69.92,100.08c0,-3.64 -0.65,-7.13 -1.77,-10.42L31.86,10.54c-1.12,-3.27 -1.78,-6.78 -1.78,-10.46");
            pathStrings.put("05","M30,0L30,100");
            pathStrings.put("70","m0,30c16.57,0 30,-13.43 30,-30");
            pathStrings.put("21","M100,30C83.43,30 70,16.57 70,0");
            pathStrings.put("31","m100,69.84c-16.57,0 -30,-13.43 -30,-30L70,0");
            pathStrings.put("14","M70,0L70,100");
            pathStrings.put("15","m69.92,0.08c0,3.64 -0.65,7.13 -1.77,10.42l-36.29,79.13c-1.12,3.27 -1.78,6.78 -1.78,10.46");
            pathStrings.put("61","m0,70c8.32,0 15.85,-3.39 21.29,-8.86l40,-40C66.67,15.71 70,8.25 70,0");
            pathStrings.put("17","m69.84,0c0,16.57 -13.43,30 -30,30L0,30");
            pathStrings.put("32","m100,70c-11.05,0 -20,-8.95 -20,-20 0,-11.05 8.95,-20 20,-20");
            pathStrings.put("24","m100,30.16c-16.57,0 -30,13.43 -30,30L70,100");
            pathStrings.put("25","m100,30c-8.32,0 -15.85,3.39 -21.29,8.86l-40,40C33.33,84.29 30,91.75 30,100");
            pathStrings.put("26","m100,30.16c-3.64,0 -7.13,0.65 -10.42,1.77l-79.13,36.29c-3.27,1.12 -6.78,1.78 -10.46,1.78");
            pathStrings.put("72","M0,30L100,30");
            pathStrings.put("34","m100,70c-16.57,0 -30,13.43 -30,30");
            pathStrings.put("53","m30.16,100c0,-16.57 13.43,-30 30,-30L100,70");
            pathStrings.put("63","M0,70L100,70");
            pathStrings.put("37","m100,70c-3.64,0 -7.13,-0.65 -10.42,-1.77L10.46,31.94C7.19,30.82 3.67,30.16 0,30.16");
            pathStrings.put("45","m70,100c0,-11.05 -8.95,-20 -20,-20 -11.05,0 -20,8.95 -20,20");
            pathStrings.put("46","m69.84,100c0,-16.57 -13.43,-30 -30,-30L0,70");
            pathStrings.put("74","m0,30c8.32,0 15.85,3.39 21.29,8.86l40,40C66.67,84.29 70,91.75 70,100");
            pathStrings.put("65","m0,70c16.57,0 30,13.43 30,30");
            pathStrings.put("75","m0,30.16c16.57,0 30,13.43 30,30L30,100");
            pathStrings.put("67","m0,70c11.05,0 20,-8.95 20,-20 0,-11.05 -8.95,-20 -20,-20");
        }

        keys.addAll(pathStrings.keySet());

        PathParser parser = new PathParser();

        for (Map.Entry<String, String> entry : pathStrings.entrySet()) {
            paths.put(entry.getKey(), parser.createPathFromPathData(entry.getValue()));
        }
    }

    public Path getPath(String key){
        Character fromPos = key.charAt(0);
        Character toPos = key.charAt(1);
        Path p = new Path();
        if (paths.containsKey(key)){
            p = paths.get(key);
        } else if (paths.containsKey(toPos+""+fromPos)) {
            p = paths.get(toPos+""+fromPos);
        }
        return p;
    }

    public Pair<float[],float[]> getPosTan(int from, int to, float rel){

        Path p = new Path();
        Matrix m = new Matrix();
        boolean fEnd = false;
        String keyStart = String.valueOf(from) + String.valueOf(to);
        String keyEnd = String.valueOf(to) + String.valueOf(from);
        if (paths.containsKey(keyStart)){
            p = paths.get(keyStart);
        } else if (paths.containsKey(keyEnd)) {
            p = paths.get(keyEnd);
            fEnd = true;
        }
        //Log.d("path",String.valueOf(from) + "," + String.valueOf(to) + "," + String.valueOf(fEnd));
        PathMeasure pm = new PathMeasure(p, false);
        float[] pos = new float[2];
        float[] tan = new float[2];
        if (fEnd){
            pm.getPosTan(pm.getLength() - rel*pm.getLength(), pos, tan);
            //Log.d("detPath",String.valueOf(pm.getLength() - rel*pm.getLength()));
        } else {
            pm.getPosTan(rel*pm.getLength(), pos, tan);
            //Log.d("detPath",String.valueOf(rel*pm.getLength()));
        }
        // width of path tiles is 100
        pos[0] = pos[0]/100f;
        pos[1] = pos[1]/100f;
        if (fEnd){
            tan[0] *= -1;
            tan[1] *= -1;
        }
        //Log.d("posPath", String.valueOf(pos[0]) + "," + String.valueOf(pos[1]));
        //Log.d("tanPath", String.valueOf(tan[0]) + "," + String.valueOf(tan[1]));
        return Pair.create(pos, tan);
    }

    public Matrix getMatrix(int from, int to, float rel){

        Path p = new Path();
        Matrix m = new Matrix();
        boolean fEnd = false;
        String keyStart = String.valueOf(from) + String.valueOf(to);
        String keyEnd = String.valueOf(to) + String.valueOf(from);
        if (paths.containsKey(keyStart)){
            p = paths.get(keyStart);
        } else if (paths.containsKey(keyEnd)) {
            p = paths.get(keyEnd);
            fEnd = true;
        }
        PathMeasure pm = new PathMeasure(p, false);
        if (fEnd){
            pm.getMatrix(pm.getLength() - rel*pm.getLength(), m, flags);
        } else {
            pm.getMatrix(rel*pm.getLength(), m, flags);
        }
        return m;
    }
}
