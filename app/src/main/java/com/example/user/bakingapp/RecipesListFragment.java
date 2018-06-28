package com.example.user.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

public class RecipesListFragment extends Fragment {
    @BindView(R.id.rv_recipes_list)
    RecyclerView mRecipesRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeToRefresh;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessage;
//    ConstraintLayout mNoDataContainer;

    private static String RECIPES_KEY = "RECIPE_LIST";

    private OnRecipeClickListener mListener;
    private Unbinder unbinder;
    private List<Recipe> mRecipes;
//    private GlobalApplication globalApplication;

    /**
     * Will load the movies when the app launch, or if the app will launch without an internet connection
     * and then reconnects, will load them without the need for user to perform a (pull to refresh)
     */
    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (mRecipes == null) {
//                loadRecipes();
//            }
        }
    };

    public RecipesListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment bind view to butter knife
 //       View viewRoot = inflater.inflate(R.layout.recipes_fragment, container, false);
//        unbinder = ButterKnife.bind(this, viewRoot);

        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                loadRecipes();
            }
        });

//        mNoDataContainer.setVisibility(View.VISIBLE);
//        setupRecyclerView();

        // Get the IdlingResource instance
//        globalApplication = (GlobalApplication) getActivity().getApplicationContext();
//
//        globalApplication.setIdleState(false);


        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES_KEY)) {
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPES_KEY);

//            mRecipesRecyclerView.setAdapter(new RecipeAdapter(getActivity().getApplicationContext(), mRecipes, new Listeners.OnItemClickListener() {
//                @Override
//                public void onItemClick(int position) {
//                    mListener.onRecipeSelected(mRecipes.get(position));
//                }
//            }));
//            dataLoadedTakeCareLayout();
        }
//        return viewRoot;
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeClickListener) {
            mListener = (OnRecipeClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
//        Logger.d("onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mRecipes != null && !mRecipes.isEmpty())
            outState.putParcelableArrayList(RECIPES_KEY, (ArrayList<? extends Parcelable>) mRecipes);
    }

    private void setupRecyclerView() {
//        mRecipesRecyclerView.setVisibility(View.GONE);
//        mRecipesRecyclerView.setHasFixedSize(true);
//
//        boolean twoPaneMode = getResources().getBoolean(R.bool.twoPaneMode);
//        if (twoPaneMode) {
//            mRecipesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 3));
//        } else {
//            mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//        }
//
//        mRecipesRecyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.margin_medium)));
//        mRecipesRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    }

    private void loadRecipes() {
        // Set SwipeRefreshLayout that refreshing in case that loadRecipes get called by the networkChangeReceiver
//        if (Misc.isNetworkAvailable(getActivity().getApplicationContext())) {
//            mPullToRefresh.setRefreshing(true);
//
//            RecipesApiManager.getInstance().getRecipes(new RecipesApiCallback<List<Recipe>>() {
//                @Override
//                public void onResponse(final List<Recipe> result) {
//                    if (result != null) {
//                        mRecipes = result;
//                        mRecipesRecyclerView.setAdapter(new RecipesAdapter(getActivity().getApplicationContext(), mRecipes, new Listeners.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(int position) {
//                                mListener.onRecipeSelected(mRecipes.get(position));
//                            }
//                        }));
//                        // Set the default recipe for the widget
//                        if (Prefs.loadRecipe(getActivity().getApplicationContext()) == null) {
//                            AppWidgetService.updateWidget(getActivity(), mRecipes.get(0));
//                        }
//
//                    } else {
//                        Misc.makeSnackBar(getActivity(), getView(), getString(R.string.failed_to_load_data), true);
//                    }
//
//                    dataLoadedTakeCareLayout();
//                }
//
//                @Override
//                public void onCancel() {
//                    dataLoadedTakeCareLayout();
//                }
//
//            });
//        } else {
//            Misc.makeSnackBar(getActivity(), getView(), getString(R.string.no_internet), true);
//        }
    }


    /**
     * Check if data is loaded and show/hide Recipes RecyclerView & NoDataContainer regarding the recipes data state
     */
    private void dataLoadedTakeCareLayout() {
        boolean loaded = mRecipes != null && mRecipes.size() > 0;
        mSwipeToRefresh.setRefreshing(false);

        mRecipesRecyclerView.setVisibility(loaded ? View.VISIBLE : View.GONE);
//        mNoDataContainer.setVisibility(loaded ? View.GONE : View.VISIBLE);
//
//        globalApplication.setIdleState(true);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }
}

