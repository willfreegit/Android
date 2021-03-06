package com.example.wechatmoments.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.wechatmoments.R;
import com.lzy.ninegrid.NineGridView;

public class GlideImageLoader implements NineGridView.ImageLoader {

    private GlideImageLoader(){
        super();
    }

    private static GlideImageLoader glideImageLoader;

    public static GlideImageLoader getGlideImageLoader(){
        if(glideImageLoader == null){
           glideImageLoader = new GlideImageLoader();
        }
        return glideImageLoader;
    }

    //@Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .error(R.drawable.barron)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.barron)
                .into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}