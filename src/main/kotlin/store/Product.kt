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
    var need = 0
    var inventoryRate = product.stock / product.proper
    if( inventoryRate <= stockThreshold){
        need = product.proper - product.stock
    }
    println("${product.name}: 현재 ${product.stock}개 -> 적정재고 ${product.proper} (${need}개 발주 필요) [재고율: $inventoryRate%]")
}

val stockThreshold = 0.3 // 재고 30% 이하 시 발주 알림