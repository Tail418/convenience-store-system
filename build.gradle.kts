plugins {
    kotlin("jvm") version "2.2.0"
    application
}

group = "bible"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}

application{
    mainClass.set("store.MainKt")
}

// JAR 파일을 만들 때 Manifest 정보와 함께,
// 실행에 필요한 모든 라이브러리(dependencies)를 포함하도록 설정
tasks.jar {
    manifest {
        attributes["Main-Class"] = "store.MainKt"
    }
    // 이 부분을 추가하거나 기존 내용을 교체합니다.
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}