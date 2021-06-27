package com.example.cetty.derailer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cetty.derailer.objects.PlayerSelection;
import com.example.cetty.derailer.objects.Theme;

import java.util.ArrayList;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.DataObjectHolder> {

    private ArrayList<PlayerSelection> mPlayers;
    private Theme mTheme;
    private static Context mContext;

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        RelativeLayout rl_cart;
        ImageView iv_cart;
        ImageView iv_cart_color;
        ImageView iv_indicator;

        public DataObjectHolder(View itemView) {
            super(itemView);
            rl_cart = (RelativeLayout) itemView.findViewById(R.id.rl_cart);
            iv_cart = (ImageView) itemView.findViewById(R.id.iv_cart);
            iv_cart_color = (ImageView) itemView.findViewById(R.id.iv_cart_color);
            iv_indicator = (ImageView) itemView.findViewById(R.id.iv_indicator);
        }
    }

    public AdapterCart(Context mContext, Theme theme, ArrayList<PlayerSelection> players) {
        this.mContext = mContext;
        this.mTheme = theme;
        this.mPlayers = players;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.rl_cart.setTag(position);
        holder.rl_cart.setOnDragListener(new MyDragListener());
        Matrix m = new Matrix();
        m.postRotate(90);
        float scale = mContext.getResources().getFraction(R.fraction.cart_scale, 1, 1);
        m.postScale(scale, scale);
        PlayerSelection player = mPlayers.get(position);

        Bitmap cart = Utilities.drawableToBitmap(mTheme.getCartResId(mContext));
        int width = cart.getWidth();
        int height = cart.getHeight();
        holder.iv_cart.setImageBitmap(Bitmap.createBitmap(cart , 0, 0, width, height, m, true));

        int color = player.getColor();
        Bitmap cart_color = Utilities.drawableToBitmap(mTheme.getCartColorResId(mContext));
        holder.iv_cart_color.setImageBitmap(Bitmap.createBitmap(cart_color , 0, 0, width, height, m, true));
        holder.iv_cart_color.setColorFilter(color);

        if (player.isUnselected()){
            holder.iv_indicator.setVisibility(View.INVISIBLE);
        } else if (player.isPlayer()){
            holder.iv_indicator.setImageResource(R.drawable.player_indicator);
            holder.iv_indicator.setVisibility(View.VISIBLE);
        } else if (player.isEasyAI()){
            holder.iv_indicator.setImageResource(R.drawable.ai_easy_indicator);
            holder.iv_indicator.setVisibility(View.VISIBLE);
        } else if (player.isNormalAI()){
            holder.iv_indicator.setImageResource(R.drawable.ai_normal_indicator);
            holder.iv_indicator.setVisibility(View.VISIBLE);
        } else if (player.isHardAI()){
            holder.iv_indicator.setImageResource(R.drawable.ai_hard_indicator);
            holder.iv_indicator.setVisibility(View.VISIBLE);
        }


    }

    public void update(){
        notifyDataSetChanged();
        StartMenuActivity.displayPlayerNumber();
    }


    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    class MyDragListener implements View.OnDragListener {
        //Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
        //Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            View dragView = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    //View view = (View) event.getLocalState();
                    //ViewGroup owner = (ViewGroup) view.getParent();
                    //owner.removeView(view);
                    //LinearLayout container = (LinearLayout) v;
                    //container.addView(view);
                    //view.setVisibility(View.VISIBLE);
                    mPlayers.get((int) v.getTag()).setSelection((String) dragView.getTag());
                    update();
                    dragView.setVisibility(View.VISIBLE);
                    Log.d("DragListener",String.valueOf(v.getTag()));
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackgroundDrawable(normalShape);
                    if (dropEventNotHandled(event)) {
                        Log.d("DragListener","Drag ended and not handeled");
                        dragView.setVisibility(View.VISIBLE);
                    }
                default:
                    break;
            }
            return true;
        }

        private boolean dropEventNotHandled(DragEvent dragEvent) {
            return !dragEvent.getResult();
        }
    }

}
