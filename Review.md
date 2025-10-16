# 좋았던 점

이런 과제를 통해서 배웠던 것들을 복습하는 시간을 가질 수 있어서 좋았습니다.
혼자 이렇게 만들어보니까 코틀린에 대해 많이 알아나갔던 것 같습니다.
지식에서 끝나는 것이 아니라 온전히 내것으로 만들어 나가야 그것이 진정한 공부 라는 것을 알게되었습니다.
개인적으로 많이 어려웠지만 이러한 과제를 또 하고 싶다 라는 생각이 들었습니다.
---
# 배운 것
### Product class 에서 int를 사용하는 이유 :
data class Product(
val name: String,
val price: Int,
val category: ProductCategory,
val optimalStock: Int, // 시스템이 권장하는 적정 재고량
var currentStock: Int, // 현재 재고량
val expiryDate: LocalDate? = null // 유통기한 (식품류가 아닌 경우 null일 수 있음)
)

수학적 계산이 필요한 숫자 데이터이기 때문이다
string의 경우 계산이 필요없는 문자열(텍스트)를 다룰 때 사용한다.

### LocalDate 란
시간이나 시간대 없이 오직 날짜를 표현하기 위해 사용하는 클래스다
날짜 정보를 정확하게 다룰 때 사용함
import java.time.temporal.ChronoUnit


### 재고율 계산 함수
currentStock = 현재 재고량
optimalStock = 적정 재고량
.toDouble 함수를 사용하는 이유는 두 변수가 Int 형인데 Int 나누기 Int 하면 값이 0이 나오기 때문에 currentStock 의 변수를 Double 로 바꾸어 계산합니다
현재 재고 나누기 적정재고 하면 재고율을 구할 수 있다
ex) 현재재고 = 5, 적정재고 = 30 -> 5.0/30 ‎ = 0.167 -> 재고율 16.7%

### Boolean(불리언) 형태란
참 또는 거짓 이 두가지 값만 가질 수 있는 데이터 타입이다

### 재고 부족 여부를 확인하는 함수
threshold = 기준점
기준점은 설정해야함
val stockThreshold = 0.3
product.isLowStock(0.3) 으로 함수 호출

### Long 타입
int 보다 훨씬 더 큰 정수를 저장할 수 있는 데이터 타입
Long 타입을 사용한 이유! => ChronoUnit.DAYS.between(시작일, 종료일) 함수를 사용하기 때문이다. 이 함수는 시간 계산 결과값을 항상 Long 타입으로 반환하도록 설계함. ChronoUnit 은 일(DAYS), 초, 천분의 일초, 십업분의 일초등 아주 작은 단위도 계산 할 수 있다.

### 유통기한 남은 날짜 계산
return this.expiryDate?.let {
ChronoUnit.DAYS.between(LocalDate.now(), it)
}

### ?. -> NULL 값이라면 NULL 값 반환
it 의미는 이 함수에서 expiryDate를 의미한다.
let 블록 안에 있는 계산식을 거쳐 Long 타입으로 반환


### 할인정보 계산하는 함수
policy = 정책 = 기준점 = 몇일 남으면 얼마나 할인해주는지 계산

fun Product.getDiscountInfo(policy: Map<Int, Double>): Pair<Double, Int>? {

입력값과 출력값을 나눠서 생각하기
- 함수(***) : ___
- 함수(___) : ***

.let 은 앞에서 찾은 값이 있으면 그 값을 {  } 블록 안으로 전달해줄게 라는 의미이다.
policy[0] 에서 찾은 값을 rate에 넣어줌

### .entries
mapOf 에 있는 key 와 value 를 한쌍으로 묶는 항목들의 목록을 반환한다.
.find 는 목록의 항목들을 하나씩 꺼내보면서 {   } 안에 있는 조건에 만족하는 항목을 찾아주는 함수이다.

### ?:
엘비스 연산자
~까지의 결과 null 이면 ___값을 반환해

### ?.value
안전호출 덕분에 value 속성을 가져오는 대신 null을 반환했다면 이 단계 전체가 null 이 됩니다

### Map 의 개념
key, value
key를 이용해서 value를 저장하고 꺼내온다


### .find { entry -> it <= entry.key }
내가 받은 상품의 남은 날(it)이 entry.key보다 작거나 같나?

### InventoryManager.kt 파일에 import 된 것
import java.text.NumberFormat
import java.util.Locale
숫자를 각 나라에 맞는 화폐 형식으로 바꾸기 위해서

import kotlin.math.roundToInt
소수점 숫자를 반올림하기 위해서

### InventoryManager 라는 객체
lateinit 나중에 초기화 해줄게 - 빈 상자 상태
객체가 만들어지는 시점에 값을 넣어줄 수 없고 나중에 다른 함수로 값을 설정해야하는 때가 있다.
main 함수를 통해 데이터가 만들어짐

### String.format("%.1f", ...)
이 숫자를 소수점 첫째 자리까지만 보여줘

### .fliter, .forEach 함수
.fliter 필터링하여 새로운 목록 만들기
.forEach 처음부터 끝까지 하나씩 꺼내서 주어진 작업 수행

### .sortedBy 함수
오름차순 정렬하여 새로운 리스트를 만드는 것입니다
.sortedByDescending{it.third}
세번째 요소 즉 총매출을 기준으로 내림차순으로 정렬

### Triple(   )
찾은 상품의 (이름, 판매량, 총매출) 하나의 묶음으로 만든다

### .forEachIndexed{     ,    }
순위를 매겨 출력한다

### .take(5)
맨 위에 있는 5개만 쏙 골라낸다

---
# 놀라왔던 것
제가 느끼기에 코틀린이 다른 언어에 비해 편한 것 같습니다.
코틀린 개발을 하면서 이렇게 코드를 줄이고 함수들을 사용해보니 놀라웠습니다.
---

# 부족했던 것
github에 대해 많이 몰랐습니다. 코틀린 함수들을 몰라서 많이 찾아보고 배웠습니다.
데이터 타입도 Lond, String, Int 왜 이 때는 이렇게 써야하는지 모르겠어서 시간을 잡아먹었습니다.
혼자 스스로 만들어내는 것이 어려웠습니다.
스스로 해보다가 포기하고 ai의 도움을 받아서 완성해서 아쉽습니다.
