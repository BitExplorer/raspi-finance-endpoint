package finance.helpers

import finance.domain.AccountType
import finance.domain.ReoccurringType
import finance.domain.Transaction
import finance.domain.TransactionState

import java.math.RoundingMode
import java.sql.Date

class TransactionBuilder {
    String guid = '4ea3be58-3993-abcd-88a2-4ffc7f1d73bd'
    Long accountId = 0
    AccountType accountType = AccountType.Credit
    String accountNameOwner = 'chase_brian'
    Date transactionDate = new Date(1553645394)
    String description = 'aliexpress.com'
    String category = 'online'
    BigDecimal amount = new BigDecimal('3.14').setScale(2, RoundingMode.HALF_UP)
    TransactionState transactionState = TransactionState.Cleared
    Boolean reoccurring = false
    ReoccurringType reoccurringType = ReoccurringType.Undefined
    String notes = 'my note to you'

    static TransactionBuilder builder() {
        return new TransactionBuilder()
    }

    Transaction build() {
        Transaction transaction = new Transaction()
        transaction.guid = guid
        transaction.accountId = accountId
        transaction.accountType = accountType
        transaction.accountNameOwner = accountNameOwner
        transaction.transactionDate = transactionDate
        transaction.description = description
        transaction.category = category
        transaction.amount = amount
        transaction.transactionState = transactionState
        transaction.reoccurring = reoccurring
        transaction.reoccurringType = reoccurringType
        transaction.notes = notes
        return transaction
    }

    TransactionBuilder guid(guid) {
        this.guid = guid
        return this
    }

    TransactionBuilder accountId(accountId) {
        this.accountId = accountId
        return this
    }

    TransactionBuilder accountType(accountType) {
        this.accountType = accountType
        return this
    }

    TransactionBuilder accountNameOwner(accountNameOwner) {
        this.accountNameOwner = accountNameOwner
        return this
    }

    TransactionBuilder transactionDate(transactionDate) {
        this.transactionDate = transactionDate
        return this
    }

    TransactionBuilder description(description) {
        this.description = description
        return this
    }

    TransactionBuilder category(category) {
        this.category = category
        return this
    }

    TransactionBuilder amount(amount) {
        this.amount = amount
        return this
    }

    TransactionBuilder transactionState(transactionState) {
        this.transactionState = transactionState
        return this
    }

    TransactionBuilder reoccurring(reoccurring) {
        this.reoccurring = reoccurring
        return this
    }

    TransactionBuilder reoccurringType(reoccurringType) {
        this.reoccurringType = reoccurringType
        return this
    }

    TransactionBuilder notes(notes) {
        this.notes = notes
        return this
    }
}
