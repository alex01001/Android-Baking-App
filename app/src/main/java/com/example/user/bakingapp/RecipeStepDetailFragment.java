package com.example.user.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepDetailFragment extends Fragment {
    public static final String STEP_KEY = "step";
    private static final String POSITION_KEY = "pos";
    private static final String PLAY_WHEN_READY_KEY = "play_when_ready";

    @BindView(R.id.instructions_container)
    NestedScrollView mInstructionsContainer;


    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView exoPlayerView;
    @BindView(R.id.step_thumbnail_image)
    ImageView mIvThumbnail;
    @BindView(R.id.instruction_text)
    TextView mTvInstructions;

    private SimpleExoPlayer exoPlayer;
    private Step mStep;
    private Unbinder unbinder;

    private long mCurrentPosition = 0;
    private boolean mPlayWhenReady = true;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(STEP_KEY)) {
            mStep = getArguments().getParcelable(STEP_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            mCurrentPosition = savedInstanceState.getLong(POSITION_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }

        mTvInstructions.setText(mStep.getDescription());

        if (!mStep.getThumbnailURL().isEmpty()) {
            Picasso.with(getContext()).load(mStep.getThumbnailURL()).fit().into(mIvThumbnail);
            mIvThumbnail.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mStep.getVideoURL()))
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        else {
            mInstructionsContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(POSITION_KEY, mCurrentPosition);
        outState.putBoolean(PLAY_WHEN_READY_KEY, mPlayWhenReady);
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            exoPlayerView.setPlayer(exoPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            exoPlayer.prepare(videoSource);

            if (mCurrentPosition != 0)
                exoPlayer.seekTo(mCurrentPosition);

            exoPlayer.setPlayWhenReady(mPlayWhenReady);
            exoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            mPlayWhenReady = exoPlayer.getPlayWhenReady();
            mCurrentPosition = exoPlayer.getCurrentPosition();

            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
