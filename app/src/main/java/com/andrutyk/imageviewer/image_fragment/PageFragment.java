package com.andrutyk.imageviewer.image_fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andrutyk.imageviewer.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 23.08.2016.
 */
public class PageFragment extends Fragment {

    private static final String ARGUMENT_PAGE_URL = "arg_page_url";
    private static final String ARGUMENT_PAGE_COMMENT = "arg_page_comment";

    private String url;
    private String comment;

    private DisplayImageOptions options;

    private Unbinder unbinder;

    @BindView(R.id.ivImage) ImageView ivImage;
    @BindView(R.id.pbLoading) ProgressBar pbLoading;
    @BindView(R.id.tvCommentFragment) TextView tvCommentFragment;

    public static PageFragment newInstance(String url, String comment) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PAGE_URL, url);
        arguments.putString(ARGUMENT_PAGE_COMMENT, comment);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(ARGUMENT_PAGE_URL);
        comment = getArguments().getString(ARGUMENT_PAGE_COMMENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, null);
        unbinder = ButterKnife.bind(this, view);
        registerForContextMenu(ivImage);
        tvCommentFragment.setText(comment);
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_empty)
                .showImageOnFail(R.mipmap.ic_error)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        ImageLoader.getInstance().displayImage(url, ivImage, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Toast.makeText(view.getContext(), getErrMessage(failReason), Toast.LENGTH_SHORT).show();
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                pbLoading.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    public String getErrMessage(FailReason failReason) {
        String message = null;
        switch (failReason.getType()) {
            case IO_ERROR:
                message = getString(R.string.input_output_error);
                break;
            case DECODING_ERROR:
                message = getString(R.string.image_cant_be_decoded);
                break;
            case NETWORK_DENIED:
                message = getString(R.string.downloads_are_denied);
                break;
            case OUT_OF_MEMORY:
                message = getString(R.string.out_of_memory_error);
                break;
            case UNKNOWN:
                message = getString(R.string.unknown_error);
                break;
        }
        return message;
    }
}
