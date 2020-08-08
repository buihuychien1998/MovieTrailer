package com.example.mvvmmovieapp.ui.search;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmmovieapp.R;
import com.example.mvvmmovieapp.data.api.TheMovieDBClient;
import com.example.mvvmmovieapp.data.api.TheMovieDBInterface;
import com.example.mvvmmovieapp.data.repository.NetworkState;
import com.example.mvvmmovieapp.ui.popular_movie.MainActivity;

public class SearchActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
    RecyclerView recyclerView;
    private SearchActivityViewModel viewModel;
    private SearchPagedListRepository repository;
    private EditText editText;
    private ImageButton btnSearch, btnBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText = findViewById(R.id.editextQuery);
        recyclerView = findViewById(R.id.rcSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBackSearch);
        progressBar = findViewById(R.id.progress_bar_search);
        textView = findViewById(R.id.txt_error_search);

        TheMovieDBInterface appService = TheMovieDBClient.getClient();

        repository = new SearchPagedListRepository(appService);


        final PopularMoviePagedListAdapter movieAdapter =
                new PopularMoviePagedListAdapter(this);

//        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
//                try {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Toast.makeText(SearchActivity.this, "Please Enter Text", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SearchActivity.this, "Click " + editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (viewModel != null) {
//                            viewModel.setQuery(editText.getText().toString());
                        viewModel = null;
                    }
                    viewModel = getViewModel(editText.getText().toString());
                    Log.d("TAG Query", viewModel.getQuery());
                    viewModel.getMoviePagedList().observe(SearchActivity.this, movies -> {
                        movieAdapter.submitList(movies);
                        Log.d("TAG", movies.toString());
                    });

                    viewModel.getNetworkState().observe(SearchActivity.this, networkState -> {
                        if (viewModel.listIsEmpty() && networkState == NetworkState.LOADED) {
                            progressBar.setVisibility(View.VISIBLE);
                        } else {
                            progressBar.setVisibility(View.GONE);

                        }
                        if (viewModel.listIsEmpty() && networkState == NetworkState.ERROR) {
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            textView.setVisibility(View.GONE);

                        }

                        if (!viewModel.listIsEmpty()) {
                            movieAdapter.setNetworkState(networkState);
                        }
                    });
                    viewModel.clear();
                    movieAdapter.submitList(null);
                }

//                } catch (Exception e) {
//                    Toast.makeText(SearchActivity.this, "" + e.getMessage() + e.getCause(), Toast.LENGTH_LONG).show();
//                }

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private SearchActivityViewModel getViewModel(final String query) {
        return new ViewModelProvider(this, new ViewModelProvider.Factory() {

            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SearchActivityViewModel(repository, query);
            }
        }).get(SearchActivityViewModel.class);
    }


}
