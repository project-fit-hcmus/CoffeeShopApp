plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}



android {
    namespace = "com.example.myjavaapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myjavaapp"
        minSdk = 28
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
}

dependencies {

    //noinspection GradleCompatible,GradleCompatible
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
//    implementation("com.google.firebase:firebase-auth")

//    implementation ("com.facebook.android:facebook-login:latest.release"
    implementation ("com.facebook.android:facebook-android-sdk:latest.release")

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-database:21.0.0")
//    implementation("com.google.firebase:firebase-database-ktx:20.1.0")



    // TODO: Add the dependencies for any other Firebase products you want to use
    // See https://firebase.google.com/docs/android/setup#available-libraries
    // For example, add the dependencies for Firebase Authentication and Cloud Firestore
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha03")
    implementation ("androidx.credentials:credentials:1.2.2")
    implementation ("com.google.android.libraries.identity.googleid:googleid:latest.release")
    implementation ("com.google.android.gms:play-services-auth:21.1.1")

    // tạo viền tròn bo góc cho hình ảnh đại diện
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation("de.hdodenhof:circleimageview:2.1.0")

    //using gooole location api

//    //noinspection GradleCompatible
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.2.0")
    implementation ("com.google.android.gms:play-services-places:17.0.0")
    implementation ("com.google.android.gms:play-services-vision:20.1.3")


    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.google.firebase:firebase-storage:21.0.0")



}