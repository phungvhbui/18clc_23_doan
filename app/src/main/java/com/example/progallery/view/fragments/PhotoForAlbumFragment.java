package com.example.progallery.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.progallery.R;
import com.example.progallery.helpers.ColumnCalculator;
import com.example.progallery.helpers.Constant;
import com.example.progallery.view.activities.MainActivity;
import com.example.progallery.view.activities.SettingsActivity;
import com.example.progallery.view.activities.ViewImageActivity;
import com.example.progallery.view.activities.ViewVideoActivity;
import com.example.progallery.view.adapters.PhotoAdapter;
import com.example.progallery.viewmodel.MediaViewModel;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.progallery.helpers.Constant.FLEX;
import static com.example.progallery.helpers.Constant.GRID;
import static com.example.progallery.helpers.Constant.LIST;
import static com.example.progallery.helpers.Constant.REQUEST_MOVE_VAULT;
import static com.example.progallery.helpers.Constant.REQUEST_REMOVE_MEDIA;
import static com.example.progallery.helpers.Constant.REQUEST_REMOVE_VAULT;
import static com.example.progallery.helpers.Constant.REQUEST_VIEW_MEDIA;

public class PhotoForAlbumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String albumName;
    private MediaViewModel mediaViewModel;
    private SwipeRefreshLayout layout;
    private PhotoAdapter photoAdapter;

    public PhotoForAlbumFragment(String albumName) {
        this.albumName = albumName;
        mediaViewModel = null;
        MainActivity.showDatesBool = false;
        MainActivity.displayOption = GRID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.view_photo_for_album_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if (MainActivity.displayOption == GRID) {
            MenuItem item = menu.findItem(R.id.grid);
            item.setChecked(true);
        } else if (MainActivity.displayOption == LIST) {
            MenuItem item = menu.findItem(R.id.list);
            item.setChecked(true);
        } else if (MainActivity.displayOption == FLEX) {
            MenuItem item = menu.findItem(R.id.flex);
            item.setChecked(true);
        }
    }

    @Override
    public void onDestroy() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        TabLayout tabLayout = MainActivity.tabLayout;
        if (albumName != null) {
            if (albumName.equals("e9569439466b447c2678d48306e433f9")) {
                Objects.requireNonNull(tabLayout.getTabAt(0)).view.setClickable(true);
                Objects.requireNonNull(tabLayout.getTabAt(1)).view.setClickable(true);
            } else {
                Objects.requireNonNull(tabLayout.getTabAt(0)).view.setClickable(true);
                Objects.requireNonNull(tabLayout.getTabAt(2)).view.setClickable(true);

            }
        } else {
            Objects.requireNonNull(tabLayout.getTabAt(0)).view.setClickable(true);
            Objects.requireNonNull(tabLayout.getTabAt(1)).view.setClickable(true);
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        if (id == R.id.grid && MainActivity.displayOption != GRID) {
            MainActivity.displayOption = GRID;
            recreateFragment();
        } else if (id == R.id.list && MainActivity.displayOption != LIST) {
            MainActivity.displayOption = LIST;
            recreateFragment();
        } else if (id == R.id.flex && MainActivity.displayOption != FLEX) {
            MainActivity.displayOption = FLEX;
            recreateFragment();
        } else if (id == android.R.id.home) {
            assert getFragmentManager() != null;
            Fragment myFragment;
            if (albumName != null) {
                if (albumName.equals("e9569439466b447c2678d48306e433f9")) {
                    myFragment = getFragmentManager().findFragmentByTag("PHOTO_VAULT");
                } else {
                    myFragment = getFragmentManager().findFragmentByTag("PHOTO_ALBUM");
                }
            } else {
                myFragment = getFragmentManager().findFragmentByTag("PHOTO_FAVORITE");
            }

            FragmentTransaction trans = getFragmentManager()
                    .beginTransaction();
            assert myFragment != null;
            trans.remove(myFragment);
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            trans.commit();

            getFragmentManager().popBackStack();
        } else if (id == R.id.settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private void recreateFragment() {
        assert this.getFragmentManager() != null;
        this.getFragmentManager().beginTransaction()
                .detach(PhotoForAlbumFragment.this)
                .attach(PhotoForAlbumFragment.this)
                .commit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        TabLayout tabLayout = MainActivity.tabLayout;

        if (albumName != null) {
            if (albumName.equals("e9569439466b447c2678d48306e433f9")) {
                Objects.requireNonNull(tabLayout.getTabAt(0)).view.setClickable(false);
                Objects.requireNonNull(tabLayout.getTabAt(1)).view.setClickable(false);
            } else {
                Objects.requireNonNull(tabLayout.getTabAt(0)).view.setClickable(false);
                Objects.requireNonNull(tabLayout.getTabAt(2)).view.setClickable(false);

            }
        } else {
            Objects.requireNonNull(tabLayout.getTabAt(0)).view.setClickable(false);
            Objects.requireNonNull(tabLayout.getTabAt(1)).view.setClickable(false);
        }

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        layout = view.findViewById(R.id.refresh_layout);
        layout.setOnRefreshListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.photo_grid_view);
        recyclerView.setHasFixedSize(true);

        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);

        photoAdapter.setMediaListener(media -> {
            if (Integer.parseInt(media.getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                Intent intent = new Intent(PhotoForAlbumFragment.this.getContext(), ViewImageActivity.class);
                intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                if (albumName != null) {
                    intent.putExtra(Constant.EXTRA_VAULT, albumName.equals("e9569439466b447c2678d48306e433f9"));
                } else {
                    intent.putExtra(Constant.EXTRA_VAULT, false);
                }

                startActivityForResult(intent, REQUEST_VIEW_MEDIA);
            } else if (Integer.parseInt(media.getMediaType()) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                Intent intent = new Intent(PhotoForAlbumFragment.this.getContext(), ViewVideoActivity.class);
                intent.putExtra(Constant.EXTRA_PATH, media.getMediaPath());
                if (albumName != null) {
                    intent.putExtra(Constant.EXTRA_VAULT, albumName.equals("e9569439466b447c2678d48306e433f9"));
                } else {
                    intent.putExtra(Constant.EXTRA_VAULT, false);
                }
                startActivityForResult(intent, REQUEST_VIEW_MEDIA);
            }
        });

        if (MainActivity.displayOption == GRID) {
            View tempView = inflater.inflate(R.layout.photo_grid_item, container, false);
            ImageView tempImage = tempView.findViewById(R.id.imageView);
            int columnWidth = tempImage.getLayoutParams().width;
            int numColumn = ColumnCalculator.calculateNoOfColumns(requireContext(), columnWidth);

            GridLayoutManager glm = new GridLayoutManager(getContext(), numColumn);
            recyclerView.setLayoutManager(glm);
        } else if (MainActivity.displayOption == LIST) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else if (MainActivity.displayOption == FLEX) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
            layoutManager.setFlexWrap(FlexWrap.WRAP);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setAlignItems(AlignItems.STRETCH);
            recyclerView.setLayoutManager(layoutManager);
        }

        layout.post(() -> {
            layout.setRefreshing(true);
            try {
                loadView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return view;
    }

    @Override
    public void onRefresh() {
        try {
            loadView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (requestCode == REQUEST_VIEW_MEDIA) {
            int requestCodeFromIntent = data.getIntExtra(Constant.EXTRA_REQUEST, -1);
            if (requestCodeFromIntent == REQUEST_REMOVE_MEDIA) {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getContext(), getResources().getString(R.string.media_deleted_success), Toast.LENGTH_SHORT).show();
                    try {
                        loadView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.failed_delete_media), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCodeFromIntent == REQUEST_MOVE_VAULT) {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getContext(), getResources().getString(R.string.moved_to_vault), Toast.LENGTH_SHORT).show();
                    try {
                        loadView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.failed_to_move_to_vault), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCodeFromIntent == REQUEST_REMOVE_VAULT) {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getContext(), getResources().getString(R.string.remove_from_vault), Toast.LENGTH_SHORT).show();
                    try {
                        loadView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.failed_to_remove_from_vault), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void loadView() throws IOException {
        layout.setRefreshing(true);

        // THIS LINE CAUSES BUG, IT DIRECTS THE APPLICATION TO NON ARGUMENT CONSTRUCTOR
        // mediaViewModel = new ViewModelProvider(getActivity()).get(MediaViewModel.class);

        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(this.requireActivity().getApplication());
        mediaViewModel = new ViewModelProvider(this, factory).get(MediaViewModel.class);
        mediaViewModel.getMediasObserver().observe(getViewLifecycleOwner(), mediaList -> {
            if (mediaList == null) {
                Toast.makeText(getContext(), getResources().getString(R.string.error_fetching_data), Toast.LENGTH_SHORT).show();
            } else {
                photoAdapter.setMediaList(mediaList);
            }
        });
        if (albumName != null) {
            if (albumName.equals("e9569439466b447c2678d48306e433f9")) {
                mediaViewModel.callServiceForVaultAlbum(getContext());
            } else {
                mediaViewModel.callServiceForAlbum(getContext(), albumName);
            }
        } else {
            mediaViewModel.callServiceForFavoriteAlbum(getContext());
        }
        layout.setRefreshing(false);
    }
}