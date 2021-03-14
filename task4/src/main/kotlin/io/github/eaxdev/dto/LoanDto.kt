package io.github.eaxdev.dto

import io.github.eaxdev.model.Loan

data class LoanDto(
    val loan: String,
    val document: String,
    val amount: Double,
    val startdate: String,
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
    val sumAmountLoans: Double,
    val loans: List<LoanDto>
) {
    companion object {
        fun of(loans: List<Loan>): LoanHistoryDto {
            return LoanHistoryDto(
                countLoan = loans.size,
                sumAmountLoans = loans.sumByDouble { it.amount },
                loans = loans.map { LoanDto.of(it) }
            )
        }
    }
}
