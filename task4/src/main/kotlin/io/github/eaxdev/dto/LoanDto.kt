package io.github.eaxdev.dto

import io.github.eaxdev.model.Loan
import java.math.BigDecimal
import java.time.LocalDate

data class LoanDto(
    val loan: String,
    val amount: BigDecimal,
    val document: String,
    val startdate: LocalDate,
    val period: Int
) {
    companion object {
        fun of(loan: Loan): LoanDto {
            return LoanDto(
                loan = loan.loan,
                document = loan.docid,
                amount = loan.amount,
                startdate = loan.startdate,
                period = loan.period
            )
        }
    }
}

data class LoanHistoryDto(
    val countLoan: Int,
    val sumAmountLoans: BigDecimal,
    val loans: List<LoanDto>
) {
    companion object {
        fun of(loans: List<Loan>): LoanHistoryDto {
            return LoanHistoryDto(
                countLoan = loans.size,
                sumAmountLoans = loans.sumOf { it.amount },
                loans = loans.map { LoanDto.of(it) }
            )
        }
    }
}
