package de.badgersburrow.derailer.objects;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.Log;

import de.badgersburrow.derailer.GameView;
import de.badgersburrow.derailer.R;

import de.badgersburrow.derailer.Utilities;
import de.badgersburrow.derailer.objects.MoveAnimSecondary;

import java.util.HashMap;

/**
 * Created by reim on 27.11.16.
 */

public class GameTheme {

    private final static String TAG = "GameTheme";

    private int themeId;
    private Bitmap cart;
    private Bitmap cart_color;
    private MoveAnimSecondary animSecondary;

    HashMap<Integer, Bitmap> card_top = new HashMap<>();
    HashMap<Integer, Bitmap> card_bottom = new HashMap<>();

    public GameTheme(Context context, int themeId){
        this.themeId = themeId;
        Log.d(TAG, "Theme selected: " + themeId);

        Resources res = context.getResources();
        TypedArray carts = res.obtainTypedArray(R.array.carts);
        TypedArray carts_color = res.obtainTypedArray(R.array.carts_color);

        TypedArray cards_2301_top = res.obtainTypedArray(R.array.con4_tile01_top);
        TypedArray cards_1032_top = res.obtainTypedArray(R.array.con4_tile02_top);
        TypedArray cards_3210_top = res.obtainTypedArray(R.array.con4_tile03_top);

        TypedArray cards_2301_bottom = res.obtainTypedArray(R.array.con4_tile01);
        TypedArray cards_1032_bottom = res.obtainTypedArray(R.array.con4_tile02);
        TypedArray cards_3210_bottom = res.obtainTypedArray(R.array.con4_tile03);

        TypedArray cards_10325476_bottom = res.obtainTypedArray(R.array.con8_10325476);
        TypedArray cards_10326745_bottom = res.obtainTypedArray(R.array.con8_10326745);
        TypedArray cards_10327654_bottom = res.obtainTypedArray(R.array.con8_10327654);
        TypedArray cards_10462735_bottom = res.obtainTypedArray(R.array.con8_10462735);
        TypedArray cards_10472653_bottom = res.obtainTypedArray(R.array.con8_10472653);
        TypedArray cards_10567234_bottom = res.obtainTypedArray(R.array.con8_10567234);
        TypedArray cards_10576243_bottom = res.obtainTypedArray(R.array.con8_10576243);
        TypedArray cards_10643725_bottom = res.obtainTypedArray(R.array.con8_10643725);
        TypedArray cards_10657324_bottom = res.obtainTypedArray(R.array.con8_10657324);
        TypedArray cards_10675423_bottom = res.obtainTypedArray(R.array.con8_10675423);
        TypedArray cards_10743652_bottom = res.obtainTypedArray(R.array.con8_10743652);
        TypedArray cards_10756342_bottom = res.obtainTypedArray(R.array.con8_10756342);
        TypedArray cards_10765432_bottom = res.obtainTypedArray(R.array.con8_10765432);
        TypedArray cards_23016745_bottom = res.obtainTypedArray(R.array.con8_23016745);
        TypedArray cards_23017654_bottom = res.obtainTypedArray(R.array.con8_23017654);
        TypedArray cards_24061735_bottom = res.obtainTypedArray(R.array.con8_24061735);
        TypedArray cards_24071653_bottom = res.obtainTypedArray(R.array.con8_24071653);
        TypedArray cards_25067134_bottom = res.obtainTypedArray(R.array.con8_25067134);
        TypedArray cards_25076143_bottom = res.obtainTypedArray(R.array.con8_25076143);
        TypedArray cards_26043715_bottom = res.obtainTypedArray(R.array.con8_26043715);
        TypedArray cards_26057314_bottom = res.obtainTypedArray(R.array.con8_26057314);
        TypedArray cards_27043651_bottom = res.obtainTypedArray(R.array.con8_27043651);
        TypedArray cards_27056341_bottom = res.obtainTypedArray(R.array.con8_27056341);
        TypedArray cards_32107654_bottom = res.obtainTypedArray(R.array.con8_32107654);
        TypedArray cards_34601725_bottom = res.obtainTypedArray(R.array.con8_34601725);
        TypedArray cards_34701652_bottom = res.obtainTypedArray(R.array.con8_34701652);
        TypedArray cards_35607124_bottom = res.obtainTypedArray(R.array.con8_35607124);
        TypedArray cards_36507214_bottom = res.obtainTypedArray(R.array.con8_36507214);
        TypedArray cards_42160735_bottom = res.obtainTypedArray(R.array.con8_42160735);
        TypedArray cards_42170653_bottom = res.obtainTypedArray(R.array.con8_42170653);
        TypedArray cards_43610725_bottom = res.obtainTypedArray(R.array.con8_43610725);
        TypedArray cards_45670123_bottom = res.obtainTypedArray(R.array.con8_45670123);
        TypedArray cards_45760132_bottom = res.obtainTypedArray(R.array.con8_45760132);
        TypedArray cards_54761032_bottom = res.obtainTypedArray(R.array.con8_54761032);
        TypedArray cards_72143650_bottom = res.obtainTypedArray(R.array.con8_72143650);

        TypedArray cards_10325476_top = res.obtainTypedArray(R.array.con8_10325476_top);
        TypedArray cards_10326745_top = res.obtainTypedArray(R.array.con8_10326745_top);
        TypedArray cards_10327654_top = res.obtainTypedArray(R.array.con8_10327654_top);
        TypedArray cards_10462735_top = res.obtainTypedArray(R.array.con8_10462735_top);
        TypedArray cards_10472653_top = res.obtainTypedArray(R.array.con8_10472653_top);
        TypedArray cards_10567234_top = res.obtainTypedArray(R.array.con8_10567234_top);
        TypedArray cards_10576243_top = res.obtainTypedArray(R.array.con8_10576243_top);
        TypedArray cards_10643725_top = res.obtainTypedArray(R.array.con8_10643725_top);
        TypedArray cards_10657324_top = res.obtainTypedArray(R.array.con8_10657324_top);
        TypedArray cards_10675423_top = res.obtainTypedArray(R.array.con8_10675423_top);
        TypedArray cards_10743652_top = res.obtainTypedArray(R.array.con8_10743652_top);
        TypedArray cards_10756342_top = res.obtainTypedArray(R.array.con8_10756342_top);
        TypedArray cards_10765432_top = res.obtainTypedArray(R.array.con8_10765432_top);
        TypedArray cards_23016745_top = res.obtainTypedArray(R.array.con8_23016745_top);
        TypedArray cards_23017654_top = res.obtainTypedArray(R.array.con8_23017654_top);
        TypedArray cards_24061735_top = res.obtainTypedArray(R.array.con8_24061735_top);
        TypedArray cards_24071653_top = res.obtainTypedArray(R.array.con8_24071653_top);
        TypedArray cards_25067134_top = res.obtainTypedArray(R.array.con8_25067134_top);
        TypedArray cards_25076143_top = res.obtainTypedArray(R.array.con8_25076143_top);
        TypedArray cards_26043715_top = res.obtainTypedArray(R.array.con8_26043715_top);
        TypedArray cards_26057314_top = res.obtainTypedArray(R.array.con8_26057314_top);
        TypedArray cards_27043651_top = res.obtainTypedArray(R.array.con8_27043651_top);
        TypedArray cards_27056341_top = res.obtainTypedArray(R.array.con8_27056341_top);
        TypedArray cards_32107654_top = res.obtainTypedArray(R.array.con8_32107654_top);
        TypedArray cards_34601725_top = res.obtainTypedArray(R.array.con8_34601725_top);
        TypedArray cards_34701652_top = res.obtainTypedArray(R.array.con8_34701652_top);
        TypedArray cards_35607124_top = res.obtainTypedArray(R.array.con8_35607124_top);
        TypedArray cards_36507214_top = res.obtainTypedArray(R.array.con8_36507214_top);
        TypedArray cards_42160735_top = res.obtainTypedArray(R.array.con8_42160735_top);
        TypedArray cards_42170653_top = res.obtainTypedArray(R.array.con8_42170653_top);
        TypedArray cards_43610725_top = res.obtainTypedArray(R.array.con8_43610725_top);
        TypedArray cards_45670123_top = res.obtainTypedArray(R.array.con8_45670123_top);
        TypedArray cards_45760132_top = res.obtainTypedArray(R.array.con8_45760132_top);
        TypedArray cards_54761032_top = res.obtainTypedArray(R.array.con8_54761032_top);
        TypedArray cards_72143650_top = res.obtainTypedArray(R.array.con8_72143650_top);


        cart = Utilities.drawableToBitmap(carts.getDrawable(themeId));
        cart_color = Utilities.drawableToBitmap(carts_color.getDrawable(themeId));
        card_top.put(1032, Utilities.drawableToBitmap(cards_1032_top.getDrawable(themeId)));
        card_top.put(2301, Utilities.drawableToBitmap(cards_2301_top.getDrawable(themeId)));
        card_top.put(3210, Utilities.drawableToBitmap(cards_3210_top.getDrawable(themeId)));
        card_bottom.put(1032, Utilities.drawableToBitmap(cards_1032_bottom.getDrawable(themeId)));
        card_bottom.put(2301, Utilities.drawableToBitmap(cards_2301_bottom.getDrawable(themeId)));
        card_bottom.put(3210, Utilities.drawableToBitmap(cards_3210_bottom.getDrawable(themeId)));

        card_bottom.put(10325476, Utilities.drawableToBitmap(cards_10325476_bottom.getDrawable(themeId)));
        card_bottom.put(10326745, Utilities.drawableToBitmap(cards_10326745_bottom.getDrawable(themeId)));
        card_bottom.put(10327654, Utilities.drawableToBitmap(cards_10327654_bottom.getDrawable(themeId)));
        card_bottom.put(10462735, Utilities.drawableToBitmap(cards_10462735_bottom.getDrawable(themeId)));
        card_bottom.put(10472653, Utilities.drawableToBitmap(cards_10472653_bottom.getDrawable(themeId)));
        card_bottom.put(10567234, Utilities.drawableToBitmap(cards_10567234_bottom.getDrawable(themeId)));
        card_bottom.put(10576243, Utilities.drawableToBitmap(cards_10576243_bottom.getDrawable(themeId)));
        card_bottom.put(10643725, Utilities.drawableToBitmap(cards_10643725_bottom.getDrawable(themeId)));
        card_bottom.put(10657324, Utilities.drawableToBitmap(cards_10657324_bottom.getDrawable(themeId)));
        card_bottom.put(10675423, Utilities.drawableToBitmap(cards_10675423_bottom.getDrawable(themeId)));
        card_bottom.put(10743652, Utilities.drawableToBitmap(cards_10743652_bottom.getDrawable(themeId)));
        card_bottom.put(10756342, Utilities.drawableToBitmap(cards_10756342_bottom.getDrawable(themeId)));
        card_bottom.put(10765432, Utilities.drawableToBitmap(cards_10765432_bottom.getDrawable(themeId)));
        card_bottom.put(23016745, Utilities.drawableToBitmap(cards_23016745_bottom.getDrawable(themeId)));
        card_bottom.put(23017654, Utilities.drawableToBitmap(cards_23017654_bottom.getDrawable(themeId)));
        card_bottom.put(24061735, Utilities.drawableToBitmap(cards_24061735_bottom.getDrawable(themeId)));
        card_bottom.put(24071653, Utilities.drawableToBitmap(cards_24071653_bottom.getDrawable(themeId)));
        card_bottom.put(25067134, Utilities.drawableToBitmap(cards_25067134_bottom.getDrawable(themeId)));
        card_bottom.put(25076143, Utilities.drawableToBitmap(cards_25076143_bottom.getDrawable(themeId)));
        card_bottom.put(26043715, Utilities.drawableToBitmap(cards_26043715_bottom.getDrawable(themeId)));
        card_bottom.put(26057314, Utilities.drawableToBitmap(cards_26057314_bottom.getDrawable(themeId)));
        card_bottom.put(27043651, Utilities.drawableToBitmap(cards_27043651_bottom.getDrawable(themeId)));
        card_bottom.put(27056341, Utilities.drawableToBitmap(cards_27056341_bottom.getDrawable(themeId)));
        card_bottom.put(32107654, Utilities.drawableToBitmap(cards_32107654_bottom.getDrawable(themeId)));
        card_bottom.put(34601725, Utilities.drawableToBitmap(cards_34601725_bottom.getDrawable(themeId)));
        card_bottom.put(34701652, Utilities.drawableToBitmap(cards_34701652_bottom.getDrawable(themeId)));
        card_bottom.put(35607124, Utilities.drawableToBitmap(cards_35607124_bottom.getDrawable(themeId)));
        card_bottom.put(36507214, Utilities.drawableToBitmap(cards_36507214_bottom.getDrawable(themeId)));
        card_bottom.put(42160735, Utilities.drawableToBitmap(cards_42160735_bottom.getDrawable(themeId)));
        card_bottom.put(42170653, Utilities.drawableToBitmap(cards_42170653_bottom.getDrawable(themeId)));
        card_bottom.put(43610725, Utilities.drawableToBitmap(cards_43610725_bottom.getDrawable(themeId)));
        card_bottom.put(45670123, Utilities.drawableToBitmap(cards_45670123_bottom.getDrawable(themeId)));
        card_bottom.put(45760132, Utilities.drawableToBitmap(cards_45760132_bottom.getDrawable(themeId)));
        card_bottom.put(54761032, Utilities.drawableToBitmap(cards_54761032_bottom.getDrawable(themeId)));
        card_bottom.put(72143650, Utilities.drawableToBitmap(cards_72143650_bottom.getDrawable(themeId)));

        card_top.put(10325476, Utilities.drawableToBitmap(cards_10325476_top.getDrawable(themeId)));
        card_top.put(10326745, Utilities.drawableToBitmap(cards_10326745_top.getDrawable(themeId)));
        card_top.put(10327654, Utilities.drawableToBitmap(cards_10327654_top.getDrawable(themeId)));
        card_top.put(10462735, Utilities.drawableToBitmap(cards_10462735_top.getDrawable(themeId)));
        card_top.put(10472653, Utilities.drawableToBitmap(cards_10472653_top.getDrawable(themeId)));
        card_top.put(10567234, Utilities.drawableToBitmap(cards_10567234_top.getDrawable(themeId)));
        card_top.put(10576243, Utilities.drawableToBitmap(cards_10576243_top.getDrawable(themeId)));
        card_top.put(10643725, Utilities.drawableToBitmap(cards_10643725_top.getDrawable(themeId)));
        card_top.put(10657324, Utilities.drawableToBitmap(cards_10657324_top.getDrawable(themeId)));
        card_top.put(10675423, Utilities.drawableToBitmap(cards_10675423_top.getDrawable(themeId)));
        card_top.put(10743652, Utilities.drawableToBitmap(cards_10743652_top.getDrawable(themeId)));
        card_top.put(10756342, Utilities.drawableToBitmap(cards_10756342_top.getDrawable(themeId)));
        card_top.put(10765432, Utilities.drawableToBitmap(cards_10765432_top.getDrawable(themeId)));
        card_top.put(23016745, Utilities.drawableToBitmap(cards_23016745_top.getDrawable(themeId)));
        card_top.put(23017654, Utilities.drawableToBitmap(cards_23017654_top.getDrawable(themeId)));
        card_top.put(24061735, Utilities.drawableToBitmap(cards_24061735_top.getDrawable(themeId)));
        card_top.put(24071653, Utilities.drawableToBitmap(cards_24071653_top.getDrawable(themeId)));
        card_top.put(25067134, Utilities.drawableToBitmap(cards_25067134_top.getDrawable(themeId)));
        card_top.put(25076143, Utilities.drawableToBitmap(cards_25076143_top.getDrawable(themeId)));
        card_top.put(26043715, Utilities.drawableToBitmap(cards_26043715_top.getDrawable(themeId)));
        card_top.put(26057314, Utilities.drawableToBitmap(cards_26057314_top.getDrawable(themeId)));
        card_top.put(27043651, Utilities.drawableToBitmap(cards_27043651_top.getDrawable(themeId)));
        card_top.put(27056341, Utilities.drawableToBitmap(cards_27056341_top.getDrawable(themeId)));
        card_top.put(32107654, Utilities.drawableToBitmap(cards_32107654_top.getDrawable(themeId)));
        card_top.put(34601725, Utilities.drawableToBitmap(cards_34601725_top.getDrawable(themeId)));
        card_top.put(34701652, Utilities.drawableToBitmap(cards_34701652_top.getDrawable(themeId)));
        card_top.put(35607124, Utilities.drawableToBitmap(cards_35607124_top.getDrawable(themeId)));
        card_top.put(36507214, Utilities.drawableToBitmap(cards_36507214_top.getDrawable(themeId)));
        card_top.put(42160735, Utilities.drawableToBitmap(cards_42160735_top.getDrawable(themeId)));
        card_top.put(42170653, Utilities.drawableToBitmap(cards_42170653_top.getDrawable(themeId)));
        card_top.put(43610725, Utilities.drawableToBitmap(cards_43610725_top.getDrawable(themeId)));
        card_top.put(45670123, Utilities.drawableToBitmap(cards_45670123_top.getDrawable(themeId)));
        card_top.put(45760132, Utilities.drawableToBitmap(cards_45760132_top.getDrawable(themeId)));
        card_top.put(54761032, Utilities.drawableToBitmap(cards_54761032_top.getDrawable(themeId)));
        card_top.put(72143650, Utilities.drawableToBitmap(cards_72143650_top.getDrawable(themeId)));

        carts.recycle();
        carts_color.recycle();
        cards_2301_top.recycle();
        cards_1032_top.recycle();
        cards_3210_top.recycle();
        cards_2301_bottom.recycle();
        cards_1032_bottom.recycle();
        cards_3210_bottom.recycle();

        cards_10325476_bottom.recycle();
        cards_10326745_bottom.recycle();
        cards_10327654_bottom.recycle();
        cards_10462735_bottom.recycle();
        cards_10472653_bottom.recycle();
        cards_10567234_bottom.recycle();
        cards_10576243_bottom.recycle();
        cards_10643725_bottom.recycle();
        cards_10657324_bottom.recycle();
        cards_10675423_bottom.recycle();
        cards_10743652_bottom.recycle();
        cards_10756342_bottom.recycle();
        cards_10765432_bottom.recycle();
        cards_23016745_bottom.recycle();
        cards_23017654_bottom.recycle();
        cards_24061735_bottom.recycle();
        cards_24071653_bottom.recycle();
        cards_25067134_bottom.recycle();
        cards_25076143_bottom.recycle();
        cards_26043715_bottom.recycle();
        cards_26057314_bottom.recycle();
        cards_27043651_bottom.recycle();
        cards_27056341_bottom.recycle();
        cards_32107654_bottom.recycle();
        cards_34601725_bottom.recycle();
        cards_34701652_bottom.recycle();
        cards_35607124_bottom.recycle();
        cards_36507214_bottom.recycle();
        cards_42160735_bottom.recycle();
        cards_42170653_bottom.recycle();
        cards_43610725_bottom.recycle();
        cards_45670123_bottom.recycle();
        cards_45760132_bottom.recycle();
        cards_54761032_bottom.recycle();
        cards_72143650_bottom.recycle();

        cards_10325476_top.recycle();
        cards_10326745_top.recycle();
        cards_10327654_top.recycle();
        cards_10462735_top.recycle();
        cards_10472653_top.recycle();
        cards_10567234_top.recycle();
        cards_10576243_top.recycle();
        cards_10643725_top.recycle();
        cards_10657324_top.recycle();
        cards_10675423_top.recycle();
        cards_10743652_top.recycle();
        cards_10756342_top.recycle();
        cards_10765432_top.recycle();
        cards_23016745_top.recycle();
        cards_23017654_top.recycle();
        cards_24061735_top.recycle();
        cards_24071653_top.recycle();
        cards_25067134_top.recycle();
        cards_25076143_top.recycle();
        cards_26043715_top.recycle();
        cards_26057314_top.recycle();
        cards_27043651_top.recycle();
        cards_27056341_top.recycle();
        cards_32107654_top.recycle();
        cards_34601725_top.recycle();
        cards_34701652_top.recycle();
        cards_35607124_top.recycle();
        cards_36507214_top.recycle();
        cards_42160735_top.recycle();
        cards_42170653_top.recycle();
        cards_43610725_top.recycle();
        cards_45670123_top.recycle();
        cards_45760132_top.recycle();
        cards_54761032_top.recycle();
        cards_72143650_top.recycle();


        switch(themeId) {

            case 0 :
                // steam engine
                animSecondary = new MoveAnimSecondary(R.drawable.smoke2,
                5, 14, 0.8f, 0.0f, 1.0f, 6.0f, true, true, false);
                break;

            case 1 :
                // electric engine
                animSecondary = new MoveAnimSecondary();
                break;

            case 2 :
                // vintage car
                animSecondary = new MoveAnimSecondary();
                break;
            default :
                animSecondary = new MoveAnimSecondary();
                break;
        }


    }

    public int getThemeId() {
        return themeId;
    }

    public Bitmap getCart(){
        return cart;
    }

    public Bitmap getCart_color() { return cart_color; }

    public Bitmap getCard_top(int cardId){
        return card_top.get(cardId);
    }

    public Bitmap getCard_bottom(int cardId){
        return card_bottom.get(cardId);
    }

    public MoveAnimSecondary getMoveAnimSecondary(GameView gameView){
        MoveAnimSecondary copy = animSecondary.getCopy();
        copy.attachGameView(gameView, cart.getWidth(), cart.getHeight());
        return copy;
    }

}
