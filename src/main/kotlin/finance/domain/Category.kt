package finance.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import finance.utils.LowerCaseConverter
import org.hibernate.annotations.Proxy
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.Size


@Entity(name = "CategoryEntity")
@Proxy(lazy = false)
@Table(name = "t_category")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @field:Min(value = 0L)
        @JsonProperty
        @Column(name="category_id", nullable = false)
        var categoryId: Long,

        @field:Size(min = 1, max = 50)
        @field:Convert(converter = LowerCaseConverter::class)
        @Column(name="category", unique = true, nullable = false)
        @JsonProperty
        var category: String
) {
    constructor() : this(0L, "")

    override fun toString(): String = mapper.writeValueAsString(this)

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}

//TODO: add active_status field
//TODO: add activeStatus field
//TODO: add dateUpdated field
//TODO: add dateAdded field

