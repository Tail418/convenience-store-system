package store

import java.time.LocalDate

fun main() {
    // Phase 1: 기본 상품 데이터 정의
    val products = listOf(
        Product("새우깡", 1500, ProductCategory.SNACK, 30, 5, null),
        Product("콜라 500ml", 1500, ProductCategory.BEVERAGE, 25, 8, null),
        Product("김치찌개 도시락", 5500, ProductCategory.FOOD, 20, 3, LocalDate.now().plusDays(2)),
        Product("참치마요 삼각김밥", 1500, ProductCategory.FOOD, 15, 12, LocalDate.now().plusDays(1)),
        Product("딸기 샌드위치", 2800, ProductCategory.FOOD, 10, 2, LocalDate.now()),
        Product("물 500ml", 1000, ProductCategory.BEVERAGE, 50, 25, null),
        Product("초코파이", 3000, ProductCategory.SNACK, 20, 15, LocalDate.now().plusDays(365)), // 유통기한이 긴 과자
        Product("즉석라면", 1200, ProductCategory.FOOD, 40, 45, LocalDate.now().plusDays(30))
    )

    // 오늘의 판매 기록 데이터
    val todaySales = mapOf(
        "새우깡" to 15,
        "콜라 500ml" to 12,
        "참치마요 삼각김밥" to 10,
        "초코파이" to 8,
        "물 500ml" to 7,
        "딸기 샌드위치" to 3,
        "김치찌개 도시락" to 2
    )

    // 시스템 운영 설정값
    val stockThreshold = 0.3 // 재고 30% 이하 시 발주 알림
    val expiryWarningDays = 3 // 3일 이내 유통기한 임박 시 할인
    val discountPolicy = mapOf(
        2 to 0.3, // 2일 이하 남음: 30% 할인
        1 to 0.5, // 1일 이하 남음: 50% 할인
        0 to 0.7  // 당일(0일 이하) 남음: 70% 할인
    )

    // Phase 3: InventoryManager를 초기화하고 시스템 실행
    InventoryManager.init(products, todaySales, stockThreshold, expiryWarningDays, discountPolicy)
    InventoryManager.run()
}