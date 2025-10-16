package store

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

object InventoryManager {

    // 시스템의 모든 데이터를 보관
    private lateinit var products: List<Product>
    private lateinit var sales: Map<String, Int>
    private var stockThreshold: Double = 0.0
    private var expiryWarningDays: Int = 0
    private lateinit var discountPolicy: Map<Int, Double>

    // 숫자 포맷팅 (통화, 퍼센트)
    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.KOREA)
    private fun formatCurrency(amount: Int): String = currencyFormat.format(amount)
    private fun formatPercentage(ratio: Double): String = "${String.format("%.1f", ratio * 100)}%"

    // 시스템 초기화 함수
    fun init(
        products: List<Product>,
        sales: Map<String, Int>,
        stockThreshold: Double,
        expiryWarningDays: Int,
        discountPolicy: Map<Int, Double>
    ) {
        this.products = products
        this.sales = sales
        this.stockThreshold = stockThreshold
        this.expiryWarningDays = expiryWarningDays
        this.discountPolicy = discountPolicy
    }

    // 전체 시스템 보고서를 출력하는 메인 실행 함수
    fun run() {
        println("=== 24시간 학교 편의점 스마트 재고 관리 시스템 ===")
        generateLowStockReport()
        generateExpiryReport()
        generateBestsellerReport()
        generateSalesReport()
        generateBusinessAnalysisReport()
        generateOverallStatusReport()
    }

    // 1. 긴급 재고 알림 보고서
    private fun generateLowStockReport() {
        println("\n🚨 긴급 재고 알림 (재고율 ${formatPercentage(stockThreshold)} 이하)")
        products.filter { it.isLowStock(stockThreshold) }
            .forEach {
                val needed = it.optimalStock - it.currentStock
                println("- ${it.name}(${it.category.displayName}): 현재 ${it.currentStock}개 → 적정재고 ${it.optimalStock}개 (${needed}개 발주 필요) [재고율: ${formatPercentage(it.getStockRatio())}]")
            }
    }

    // 2. 유통기한 관리 보고서
    private fun generateExpiryReport() {
        println("\n⚠ 유통기한 관리 (${expiryWarningDays}일 이내 임박 상품)")
        products.filter { it.getDaysUntilExpiry() != null && it.getDaysUntilExpiry()!! < expiryWarningDays }
            .sortedBy { it.getDaysUntilExpiry() }
            .forEach { product ->
                val daysLeft = product.getDaysUntilExpiry()!!
                val dayText = when {
                    daysLeft < 0 -> "기한 초과"
                    daysLeft == 0L -> "당일까지"
                    else -> "$daysLeft 일 남음"
                }
                product.getDiscountInfo(discountPolicy)?.let { (rate, discountedPrice) ->
                    println("- ${product.name}: $dayText → 할인률 ${formatPercentage(rate)} 적용 (${formatCurrency(product.price)} → ${formatCurrency(discountedPrice)})")
                }
            }
    }

    // 3. 오늘의 베스트셀러 TOP 5 보고서
    private fun generateBestsellerReport() {
        println("\n📈 오늘의 베스트셀러 TOP 5")
        sales.mapNotNull { (name, quantity) ->
            products.find { it.name == name }?.let { product ->
                Triple(product.name, quantity, product.price * quantity)
            }
        }.sortedByDescending { it.third }
            .take(5)
            .forEachIndexed { index, (name, quantity, revenue) ->
                println("${index + 1}위: $name (${quantity}개 판매, 매출 ${formatCurrency(revenue)})")
            }
    }

    // 4. 매출 현황 보고서
    private fun generateSalesReport() {
        println("\n💰 매출 현황")
        val totalRevenue = sales.entries.sumOf { (name, quantity) ->
            products.find { it.name == name }?.price?.times(quantity) ?: 0
        }
        val totalItemsSold = sales.values.sum()
        println("- 오늘 총 매출: ${formatCurrency(totalRevenue)} (${totalItemsSold}개 판매)")

        sales.mapNotNull { (name, quantity) ->
            products.find { it.name == name }?.let { product ->
                Triple(name, quantity, product.price)
            }
        }.forEach { (name, quantity, price) ->
            println(" * $name: ${formatCurrency(quantity * price)} (${quantity}개 × ${formatCurrency(price)})")
        }
    }

    // 5. 경영 분석 리포트
    private fun generateBusinessAnalysisReport() {
        println("\n🎯 경영 분석 리포트 (입력 데이터 기반 분석)")

        val soldProducts = products.filter { sales.containsKey(it.name) && it.currentStock > 0 }
        val unsoldProducts = products.filter { !sales.containsKey(it.name) }

        // 재고 회전율 (판매량 / 현재 재고)
        val turnoverList = (soldProducts + unsoldProducts).map {
            val soldCount = sales.getOrDefault(it.name, 0)
            val turnover = if (it.currentStock > 0) soldCount.toDouble() / it.currentStock else 0.0
            it.name to turnover
        }.sortedByDescending { it.second }

        val bestTurnover = turnoverList.first()
        val worstTurnover = turnoverList.last()
        println("- 재고 회전율 최고: ${bestTurnover.first} (판매 ${sales.getOrDefault(bestTurnover.first, 0)}개 / 재고 ${products.find{it.name==bestTurnover.first}!!.currentStock}개 → ${formatPercentage(bestTurnover.second)} 회전)")
        println("- 재고 회전율 최저: ${worstTurnover.first} (판매 ${sales.getOrDefault(worstTurnover.first, 0)}개 / 재고 ${products.find{it.name==worstTurnover.first}!!.currentStock}개 → ${formatPercentage(worstTurnover.second)} 회전)")

        // 판매 효율 (판매량 / (판매량 + 현재 재고)) -> 더 현실적인 지표로 수정
        val efficiencyList = products.map {
            val soldCount = sales.getOrDefault(it.name, 0)
            val openingStock = soldCount + it.currentStock
            val efficiency = if (openingStock > 0) soldCount.toDouble() / openingStock else 0.0
            it.name to efficiency
        }.sortedByDescending { it.second }
        println("- 판매 효율 1위: ${efficiencyList.first().first} (초기재고 ${sales.getOrDefault(efficiencyList.first().first, 0) + products.find{it.name==efficiencyList.first().first}!!.currentStock}개 중 ${sales.getOrDefault(efficiencyList.first().first, 0)}개 판매 → ${formatPercentage(efficiencyList.first().second)} 효율)")

        // 재고 과다 품목
        val overstocked = unsoldProducts.sortedByDescending { it.currentStock }.take(2)
        println("- 재고 과다 품목: ${overstocked.joinToString { "${it.name} (${it.currentStock}개)" }}")

        // 발주 권장
        val lowStockItems = products.filter { it.isLowStock(stockThreshold) }
        val totalOrderCount = lowStockItems.sumOf { it.optimalStock - it.currentStock }
        println("- 발주 권장: 총 ${lowStockItems.size}개 품목, ${totalOrderCount}개 수량")
    }

    // 6. 종합 운영 현황 보고서
    private fun generateOverallStatusReport() {
        println("\n📋 종합 운영 현황 (시스템 처리 결과)")
        println("- 전체 등록 상품: ${products.size}종")
        val totalStockCount = products.sumOf { it.currentStock }
        println("- 현재 총 재고: ${totalStockCount}개")
        val totalStockValue = products.sumOf { it.price * it.currentStock }
        println("- 현재 재고가치: ${formatCurrency(totalStockValue)}")
        println("- 재고 부족 상품: ${products.count { it.isLowStock(stockThreshold) }}종 (${formatPercentage(stockThreshold)} 이하)")
        println("- 유통기한 임박: ${products.count { it.getDaysUntilExpiry() != null && it.getDaysUntilExpiry()!! < expiryWarningDays }}종 (${expiryWarningDays}일 이내)")
        println("- 오늘 총 판매: ${sales.values.sum()}개")
        println("- 시스템 처리 완료: 100%")
    }
}