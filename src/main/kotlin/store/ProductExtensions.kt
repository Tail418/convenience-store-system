package store

import java.time.LocalDate
import java.time.temporal.ChronoUnit

//재고율을 계산해주는 함수
fun Product.getStockRatio(): Double = if (this.optimalStock > 0) {
    this.currentStock.toDouble() / this.optimalStock
} else {
    0.0
}

// 재고 부족 여부를 확인하는 함수
fun Product.isLowStock(threshold: Double): Boolean = getStockRatio() < threshold

// 유통기한까지 남은 일수를 계산하는 함수
fun Product.getDaysUntilExpiry(): Long? {
    return this.expiryDate?.let {
        ChronoUnit.DAYS.between(LocalDate.now(), it)
    }
}

// 할인 정보를 계산하는 함수 (할인율, 할인가)
fun Product.getDiscountInfo(policy: Map<Int, Double>): Pair<Double, Int>? {
    val days = getDaysUntilExpiry()
    return days?.let {
        if (it < 0) { // 유통기한이 지난 경우
            policy[0]?.let { rate -> Pair(rate, (price * (1 - rate)).toInt()) }
        } else {
            val discountRate = policy.entries.find { entry -> it <= entry.key }?.value ?: 0.0
            if (discountRate > 0.0) {
                Pair(discountRate, (price * (1 - discountRate)).toInt())
            } else {
                null
            }
        }
    }
}