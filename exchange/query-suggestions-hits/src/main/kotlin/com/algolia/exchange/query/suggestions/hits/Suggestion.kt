package com.algolia.exchange.query.suggestions.hits

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Suggestion(
    val query: String,
    override val _highlightResult: JsonObject? = null
) : Highlightable {

    val highlightedQuery: HighlightedString?
        get() = getHighlight(Attribute("query"))
}
