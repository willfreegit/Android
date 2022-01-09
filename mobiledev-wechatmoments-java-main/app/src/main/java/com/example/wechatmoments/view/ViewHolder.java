package com.example.wechatmoments.view;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wechatmoments.data.entity.Image;
import com.example.wechatmoments.data.entity.Sender;
import com.example.wechatmoments.data.entity.Tweet;
import com.example.wechatmoments.databinding.RecyclerViewItemBinding;
import com.example.wechatmoments.imageloader.GlideImageLoader;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridViewAdapter;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.Optional;

public class ViewHolder extends RecyclerView.ViewHolder {

    private final RecyclerViewItemBinding binding;
    private GlideImageLoader glideImageLoader;

    public ViewHolder(RecyclerViewItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.glideImageLoader = GlideImageLoader.getGlideImageLoader();
    }

    public void populate(Tweet tweet) {
        if(tweet.getSender() != null && tweet.getSender().getAvatar() != null){
            binding.senderNick.setText(tweet.getSender().getNick());
            glideImageLoader.onDisplayImage(binding.getRoot().getContext(), binding.senderAvatar, tweet.getSender().getAvatar());
        }
        if(tweet.getImages() != null){
            Image[] images = tweet.getImages();
            ArrayList<ImageInfo> array = new ArrayList<>();
            for(Image image: images){
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setBigImageUrl(image.getUrl());
                imageInfo.setThumbnailUrl(image.getUrl());
                array.add(imageInfo);
            }
            if(array.size() > 0) {
                binding.nineGridView.setAdapter(new NineGridViewClickAdapter(binding.nineGridView.getContext(), array));
            }
        }
        binding.content.setText(tweet.getContent());
        binding.comments.notifyDataSetChanged();
    }
}
