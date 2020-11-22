package finance.domain

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.hibernate.annotations.Proxy
import org.hibernate.annotations.Type
import java.text.SimpleDateFormat
import javax.persistence.*
import javax.validation.constraints.Min

@Entity(name = "ReceiptImageEntity")
@Proxy(lazy = false)
@Table(name = "t_receipt_image")
@JsonIgnoreProperties(ignoreUnknown = true)
data class ReceiptImage(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @field:Min(value = 0L)
        @JsonIgnore
        @Column(name = "receipt_image_id", nullable = false)
        var receipt_image_id: Long,

        @JsonIgnore
        @field:Min(value = 0L)
        @Column(name = "transaction_id", nullable = false)
        var transactionId: Long
) {

    constructor() : this(0L, 0L)

    @JsonGetter("receiptImage")
    fun jsonGetterReceiptImage(): String {
        return this.receiptImage.toString(Charsets.UTF_8)
    }

    @Lob
    @JsonProperty
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "receipt_image", nullable = false)
    lateinit var receiptImage: ByteArray

    override fun toString(): String = mapper.writeValueAsString(this)

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}