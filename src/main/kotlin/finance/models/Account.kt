package finance.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import finance.pojos.AccountType
import finance.utils.AccountTypeConverter
import finance.utils.Constants
import java.math.BigDecimal
import javax.persistence.*
import java.sql.Timestamp
import javax.validation.constraints.Digits
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity(name = "AccountEntity")
@Table(name = "t_account")
open class Account constructor(_accountId: Long = 0L, _accountNameOwner: String = "",
                               _accountType: AccountType = AccountType.Credit,
                               _activeStatus: Boolean = true, _moniker: String = "0000",
                               _totals: BigDecimal = BigDecimal(0.0),
                               _totalsBalanced: BigDecimal = BigDecimal(0.0),
                               _dateClosed: Timestamp = Timestamp(0),
                               _dateUpdated: Timestamp = Timestamp(System.currentTimeMillis()),
                               _dateAdded: Timestamp = Timestamp(System.currentTimeMillis())
) {

    //empty secondary constructor
    constructor() : this(0L, "", AccountType.Credit, true,
            "0000", BigDecimal(0.0), BigDecimal(0.0), Timestamp(0),
            Timestamp(System.currentTimeMillis()), Timestamp(System.currentTimeMillis())) {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @Min(value = 0L)
    var accountId = _accountId

    @Column(unique=true)
    @Size(min = 3, max = 40)
    @JsonProperty
    @Pattern(regexp = Constants.ALPHA_UNDERSCORE_PATTERN, message = Constants.MUST_BE_ALPHA_UNDERSCORE_MESSAGE)
    open var accountNameOwner = _accountNameOwner

    @Convert(converter = AccountTypeConverter::class)
    @JsonProperty
    open var accountType = _accountType

    @JsonProperty
    open var activeStatus = _activeStatus

    @JsonProperty
    @Size(min = 4, max = 4)
    open var moniker = _moniker

    @JsonProperty
    @Digits(integer = 6, fraction = 2, message = Constants.MUST_BE_DOLLAR_MESSAGE)
    open var totals = _totals

    @JsonProperty
    @Digits(integer = 6, fraction = 2, message = Constants.MUST_BE_DOLLAR_MESSAGE)
    open var totalsBalanced = _totalsBalanced

    @JsonProperty
    open var dateClosed = _dateClosed

    @JsonProperty
    open var dateUpdated = _dateUpdated

    @JsonProperty
    open var dateAdded = _dateAdded

    override fun toString(): String = mapper.writeValueAsString(this)

    companion object {
        @JsonIgnore
        private val mapper = ObjectMapper()
    }
}