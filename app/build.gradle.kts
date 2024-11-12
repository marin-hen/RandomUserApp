plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.compose.compiler)
}

apply(from = "../config/gradle/build-scripts/android.gradle")

android.defaultConfig {
    applicationId = "com.example.testapp"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":feature:user"))
    implementation(project(":domain"))
    implementation(project(":data"))
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    kapt(libs.hilt.compiler)
}