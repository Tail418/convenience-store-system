package store

enum class ProductCategory(){
    SNACK,
    FOOD,
    BEVERAGE,
    OTHER
}

data class Product(
    val name: String,
    val price: Double,
    val category: ProductCategory,
    val proper: String,
    var stock: String
)

fun checkProduct(product: Product){
    var need: String
    if(product.stock / product.proper <= stockThreshold){
        need = product.proper - product.stock
    }
}

val stockThreshold = 0.3 // 재고 30% 이하 시 발주 알림