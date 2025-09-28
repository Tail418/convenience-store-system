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
    var stock: String,
    var expiration: Int
)