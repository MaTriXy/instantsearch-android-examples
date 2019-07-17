package com.algolia.instantsearch.examples.icebnb.widgets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.algolia.instantsearch.examples.icebnb.model.HitMarker;
import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.core.model.AlgoliaResultsListener;
import com.algolia.instantsearch.core.model.AlgoliaSearcherListener;
import com.algolia.instantsearch.core.model.SearchResults;
import com.algolia.search.saas.Query;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertmogos on 11/09/2017.
 */

public class MapWidget implements OnMapReadyCallback, AlgoliaSearcherListener, AlgoliaResultsListener {

    @NonNull final SupportMapFragment mapFragment;
    public GoogleMap googleMap;
    @NonNull private List<JSONObject> hits = new ArrayList<>();

    public MapWidget(@NonNull final SupportMapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateMapPOIs();
    }

    @Override
    public void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
        addHits(results, !isLoadingMore);
        if (googleMap != null) {
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override public void onMapLoaded() {
                    updateMapPOIs();
                }
            });
        }
    }

    /**
     * Adds or replaces hits to/in this widget.
     *
     * @param results     A {@link JSONObject} containing hits.
     * @param isReplacing {@code true} if the given hits should replace the current hits.
     */
    private void addHits(@Nullable SearchResults results, boolean isReplacing) {
        if (results == null) {
            if (isReplacing) {
                hits.clear();
            }
            return;
        }
        final JSONArray newHits = results.hits;
        if (isReplacing) {
           hits.clear();
        }
        for (int i = 0; i < newHits.length(); ++i) {
            final JSONObject hit = newHits.optJSONObject(i);
            if (hit != null) {
                hits.add(hit);
            }
        }
    }

    private void updateMapPOIs() {
        googleMap.clear();
        if (hits.isEmpty()) {
            return;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (final JSONObject hit: hits) {
            final MarkerOptions marker = HitMarker.marker(hit);
            builder.include(marker.getPosition());
            googleMap.addMarker(marker);
        }
        LatLngBounds bounds = builder.build();
        // update the camera
        int padding = 10;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }

    @Override public void initWithSearcher(@NonNull Searcher searcher) {
        searcher.setQuery(searcher.getQuery().setAroundRadius(Query.RADIUS_ALL));
    }
}
