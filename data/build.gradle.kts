plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
}

apply(from = "../config/gradle/build-scripts/android.gradle")

android {
    namespace = "com.example.data"

    buildTypes {
        all {
            buildConfigField(name = "API_URL", type = "String", value = "\"https://randomuser.me/\"")
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    kapt(libs.hilt.compiler)
    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.logger)
    implementation(libs.retrofit.convertor)
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.serialization)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
}