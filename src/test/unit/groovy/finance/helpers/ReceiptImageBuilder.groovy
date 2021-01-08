package finance.helpers

import finance.domain.ReceiptImage
import org.springframework.util.Base64Utils

// curl -k --header "Content-Type: application/json" 'https://localhost:8080/receipt/image/insert' -X POST -d '{"activeStatus":true,"transactionId": 23189, "jpgImage":"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mMMYfj/HwAEVwJUeAAUQgAAAABJRU5ErkJggg==" }'
// https://cryptii.com/pipes/base64-to-hex
class ReceiptImageBuilder {
    Long transactionId = 22530
    Boolean activeStatus = true
    //String jpgImage = "data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mMMYfj/HwAEVwJUeAAUQgAAAABJRU5ErkJggg=="
    String jpgImage = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mMMYfj/HwAEVwJUeAAUQgAAAABJRU5ErkJggg=="
    
    static ReceiptImageBuilder builder() {
        return new ReceiptImageBuilder()
    }

    ReceiptImage build() {
        ReceiptImage receiptImage = new ReceiptImage().with {
            it.transactionId = this.transactionId
            it.activeStatus = this.activeStatus
            //it.jpgImage = this.jpgImage.getBytes()
            it.jpgImage = Base64Utils.decodeFromString(this.jpgImage)
            return it
        }
        return receiptImage
    }

    ReceiptImageBuilder withJpgImage(String jpgImage) {
        this.jpgImage = jpgImage
        return this
    }

    ReceiptImageBuilder withTransactionId(Long transactionId) {
        this.transactionId = transactionId
        return this
    }

    ReceiptImageBuilder withActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus
        return this
    }
}
