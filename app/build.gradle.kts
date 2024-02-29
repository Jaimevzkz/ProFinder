plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //ksp
    id("com.google.devtools.ksp")
    //Hilt
    id("dagger.hilt.android.plugin")
    //FireBase
    id("com.google.gms.google-services")
    //Secrets gradle plugin (for maps)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.vzkz.profinder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vzkz.profinder"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //Unit Test
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    val coroutinesTestVersion = "1.7.1"
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTestVersion")
    //mockk
    val mockkVersion = "1.13.9"
    testImplementation("io.mockk:mockk:${mockkVersion}")

    //Ui Test
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Hilt
    val hiltVersion = "2.48.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-compiler:$hiltVersion")
    //Hilt navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    //Destinations
    val destinationsVersion = "1.9.54"
    implementation("io.github.raamcosta.compose-destinations:core:$destinationsVersion")
    ksp("io.github.raamcosta.compose-destinations:ksp:$destinationsVersion")

    //Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.6.2")

    //FireBase
    val bomVersion = "32.5.0"
    implementation(platform("com.google.firebase:firebase-bom:$bomVersion"))
    //Auth
    implementation("com.google.firebase:firebase-auth-ktx")
    //Analytics
    implementation("com.google.firebase:firebase-analytics")
    //FireStore
    implementation("com.google.firebase:firebase-firestore-ktx")
    //Storage
    implementation("com.google.firebase:firebase-storage")

    //Animation
    implementation("androidx.compose.animation:animation:1.6.2")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    //Maps
    implementation("com.google.maps.android:maps-compose:4.3.3")
    // Optionally, you can include the Compose utils library for Clustering,
    // Street View metadata checks, etc.
    implementation("com.google.maps.android:maps-compose-utils:4.3.3")
    // Optionally, you can include the widgets library for ScaleBar, etc.
    implementation ("com.google.maps.android:maps-compose-widgets:4.3.3")

    //shimmer
    implementation("com.valentinilk.shimmer:compose-shimmer:1.2.0")

}