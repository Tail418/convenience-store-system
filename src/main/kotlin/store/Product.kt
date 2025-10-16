package store

import java.time.LocalDate

//내가 처음 만든 코드
//enum class ProductCategory(){
//    SNACK,
//    FOOD,
//    BEVERAGE,
//    OTHER
//}

//Gemini 방법 - 데이터와 표현을 묶어서 사용자에게 보여줄 이름까지 설정
enum class ProductCategory(val displayName: String) {
    SNACK("과자류"),
    BEVERAGE("음료류"),
    FOOD("식품류")
}

// 상품 정보
data class Product(
    val name: String,
    val price: Int,
    val category: ProductCategory,
    val optimalStock: Int, // 적정재고
    var currentStock: Int, // 현재재고
    val expiryDate: LocalDate? = null // 유통기한 (식품류가 아닌 경우 null)
)

