import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.identity.credential.android)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation("androidx.activity:activity:1.9.1")
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
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("androidx.recyclerview:recyclerview:1.3.1")//RecyclerView

    implementation ("com.github.PhilJay:MpAndroidChart:v3.1.0") // 막대 그래프 구현 <MPAndroidChart>

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")

    implementation ("com.google.android.material:material:1.9.0")


    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:1.1.5") // 최신 버전으로 변경 가능




//    implementation("com.kizitonwose.calendar:view:2.6.0-beta04")
////    implementation("com.kizitonwose.calendar:data:2.6.0-beta04")
////    implementation("com.kizitonwose.calendar:core:2.6.0-beta04")
//    implementation("com.kizitonwose.calendar:compose-multiplatform-android:2.6.0-alpha05") //kizitonwose의 캘린더

}