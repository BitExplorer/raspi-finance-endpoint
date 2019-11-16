package finance.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import finance.pojos.AccountType
import finance.utils.AccountTypeConverter
import finance.utils.Constants.ALPHA_UNDERSCORE_PATTERN
import finance.utils.Constants.ASCII_PATTERN
import finance.utils.Constants.MUST_BE_ALPHA_UNDERSCORE_MESSAGE
import finance.utils.Constants.MUST_BE_ASCII_MESSAGE
import finance.utils.Constants.MUST_BE_DOLLAR_MESSAGE
import finance.utils.Constants.MUST_BE_UUID_MESSAGE
import finance.utils.Constants.UUID_PATTERN
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.*

@Entity(name = "TransactionEntity")
@Table(name = "t_transaction")
open class Transaction constructor(_transactionId: Long = 0L, _guid: String = "",
                                   _accountId: Long = 0, _accountType: AccountType = AccountType.Credit,
                                   _accountNameOwner: String = "", _transactionDate: Date = Date(0),
                                   _description: String = "", _category: String = "",
                                   _amount: BigDecimal = BigDecimal(0.00), _cleared: Int = 0,
                                   _reoccurring: Boolean = false, _notes: String = "",
                                   _dateUpdated: Timestamp = Timestamp(0),
                                   _dateAdded: Timestamp = Timestamp(0),
                                   _sha256: String = ""
) {

    //TODO: add active_status field

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0L)
    @JsonProperty
    var transactionId = _transactionId

    @Column(unique = true)
    @JsonProperty
    @Pattern(regexp = UUID_PATTERN, message = MUST_BE_UUID_MESSAGE)
    open var guid = _guid

    @JsonProperty
    @Min(value = 0L)
    open var accountId = _accountId

    //@Enumerated(EnumType.STRING)
    @Convert(converter = AccountTypeConverter::class)
    @Column(columnDefinition = "VARCHAR")
    @JsonProperty
    open var accountType = _accountType

    @Size(min = 3, max = 40)
    @JsonProperty
    @Pattern(regexp = ALPHA_UNDERSCORE_PATTERN, message = MUST_BE_ALPHA_UNDERSCORE_MESSAGE)
    open var accountNameOwner = _accountNameOwner

    @JsonProperty
    open var transactionDate = _transactionDate

    @Size(min = 1, max = 75)
    @Pattern(regexp = ASCII_PATTERN, message = MUST_BE_ASCII_MESSAGE)
    @JsonProperty
    open var description = _description

    @Size(max = 50)
    @JsonProperty
    @Pattern(regexp = ASCII_PATTERN, message = MUST_BE_ASCII_MESSAGE)
    open var category = _category

    @JsonProperty
    @Digits(integer = 6, fraction = 2, message = MUST_BE_DOLLAR_MESSAGE)
    open var amount = _amount

    @Min(value = -3)
    @Max(value = 1)
    @Column(name = "cleared")
    @JsonProperty
    open var cleared = _cleared

    @JsonProperty
    open var reoccurring = _reoccurring

    @Size(max = 100)
    @JsonProperty
    @Pattern(regexp = ASCII_PATTERN, message = MUST_BE_ASCII_MESSAGE)
    open var notes = _notes

    @JsonProperty
    open var dateUpdated = _dateUpdated

    @JsonProperty
    open var dateAdded = _dateAdded

    //TODO: is this a field to remove as it is not required
    @Size(max = 70)
    @JsonProperty
    open var sha256 = _sha256

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "accountId", nullable = true, insertable = false, updatable = false)
    @JsonIgnore
    open var account: Account? = null

    @ManyToMany
    @JoinTable(name = "t_transaction_categories",
            joinColumns = [JoinColumn(name = "transactionId")],
            inverseJoinColumns = [JoinColumn(name = "categoryId")])
    @JsonIgnore
    open var categries = mutableListOf<Category>()

    override fun toString(): String = mapper.writeValueAsString(this)

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}
