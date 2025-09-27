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
    val stock: String
)
