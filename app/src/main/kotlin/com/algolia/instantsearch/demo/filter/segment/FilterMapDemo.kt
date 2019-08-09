package com.algolia.instantsearch.demo.filter.segment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.android.filter.map.FilterMapViewRadioGroup
import com.algolia.instantsearch.helper.filter.map.FilterMapConnector
import com.algolia.instantsearch.helper.filter.map.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.demo_filter_segment.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterMapDemo : AppCompatActivity() {

    private val gender = Attribute("gender")
    private val groupGender = groupAnd(gender)
    private val filterState = FilterState()
    private val searcher = SearcherSingleIndex(stubIndex)
    private val filters = mapOf(
        R.id.male to Filter.Facet(gender, "male"),
        R.id.female to Filter.Facet(gender, "female")
    )
    private val filterSegment = FilterMapConnector(filters, filterState, groupID = groupGender)
    private val connection = ConnectionHandler(filterSegment, searcher.connectFilterState(filterState))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_segment)

        val viewGender = FilterMapViewRadioGroup(radioGroupGender)

        connection += filterSegment.connectView(viewGender)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, gender)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}