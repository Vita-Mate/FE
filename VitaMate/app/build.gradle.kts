import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
//    id("org.jetbrains.kotlin.android")
//    id("org.jetbrains.kotlin.android")

}

android {
    namespace = "com.my.vitamateapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.my.vitamateapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    dataBinding{
        enable = true
    }

    viewBinding {
        enable = true
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    configurations.all {
        resolutionStrategy.force ("androidx.core:core:1.13.1")
    }


}

dependencies {

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.identity.credential.android)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation("androidx.activity:activity:1.9.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0")
    implementation("androidx.room:room-common:2.6.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("com.kakao.sdk:v2-all:2.20.1") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation ("com.kakao.sdk:v2-user:2.20.1") // 카카오 로그인 API 모듈
    implementation ("com.kakao.sdk:v2-share:2.20.1") // 카카오톡 공유 API 모듈
    implementation ("com.kakao.sdk:v2-talk:2.20.1") // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
    implementation ("com.kakao.sdk:v2-friend:2.20.1") // 피커 API 모듈
    implementation ("com.kakao.sdk:v2-navi:2.20.1") // 카카오내비 API 모듈
    implementation ("com.kakao.sdk:v2-cert:2.20.1")// 카카오톡 인증 서비스 API 모듈

    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.7.0")
//    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("androidx.recyclerview:recyclerview:1.3.1")//RecyclerView

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")

    implementation ("com.google.android.material:material:1.9.0")


    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:1.1.5") // 최신 버전으로 변경 가능

    implementation ("androidx.room:room-runtime:2.5.0")
    annotationProcessor ("androidx.room:room-compiler:2.5.0")
    // Kotlin Annotation Processor

    //영양제 막대바를 위한 라이브러리 추가
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0")

    //백엔드 api사용을 위해 retrofit추가
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.github.prolificinteractive:material-calendarview:1.4.3")





//    implementation("com.kizitonwose.calendar:view:2.6.0-beta04")
////    implementation("com.kizitonwose.calendar:data:2.6.0-beta04")
////    implementation("com.kizitonwose.calendar:core:2.6.0-beta04")
//    implementation("com.kizitonwose.calendar:compose-multiplatform-android:2.6.0-alpha05") //kizitonwose의 캘린더

}