object Dependencies {
    val pagingCompose by lazy { "androidx.paging:paging-compose:${Versions.pagingCompose}" }
    val coilCompose by lazy { "io.coil-kt:coil-compose:${Versions.coilCompose}" }
    val palette by lazy { "androidx.palette:palette-ktx:${Versions.palette}" }
    val splashScreen by lazy { "androidx.core:core-splashscreen:${Versions.splashscreen}" }
    val core by lazy { "androidx.core:core-ktx:1.8.0" }
    val lifecycleRuntime by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntime}" }
    val timber by lazy { "com.jakewharton.timber:timber:${Versions.timber}" }
    val coroutinesTest by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}" }
    val truth by lazy { "com.google.truth:truth:${Versions.truth}" }
    val coreTesting by lazy { "androidx.arch.core:core-testing:${Versions.coreTesting}" }
    val mockito by lazy { "org.mockito:mockito-core:${Versions.mockito}" }
    val mockWebServer by lazy { "com.squareup.okhttp3:mockwebserver:${Versions.mockWebserver}" }
    val espresso by lazy { "androidx.test.espresso:espresso-core:${Versions.espresso}" }
    val leakCanary by lazy { "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}" }

    object ComposeDestinations {
        val animationCore by lazy { "io.github.raamcosta.compose-destinations:animations-core:${Versions.composeDestinations}" }
        val ksp by lazy { "io.github.raamcosta.compose-destinations:ksp:${Versions.composeDestinations}" }
    }

    object Room {
        val ktx by lazy { "androidx.room:room-ktx:${Versions.room}" }
        val compiler by lazy { "androidx.room:room-compiler:${Versions.room}" }
        val paging by lazy { "androidx.room:room-paging:${Versions.room}" }
    }

    object Compose {
        val ui by lazy { "androidx.compose.ui:ui:${Versions.compose}" }
        val material by lazy { "androidx.compose.material:material:${Versions.compose}" }
        val uiTooling by lazy { "androidx.compose.ui:ui-tooling-preview:${Versions.compose}" }
        val activity by lazy { "androidx.activity:activity-compose:${Versions.activityCompose}" }
        val lottie by lazy { "com.airbnb.android:lottie-compose:${Versions.lottieCompose}" }
        val constraintLayout by lazy { "androidx.constraintlayout:constraintlayout-compose:${Versions.constraintLayoutCompose}" }
        val uiTest by lazy { "androidx.compose.ui:ui-test-junit4:${Versions.compose}" }
    }

    object Accompanist {
        val systemUiController by lazy { "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}" }
        val flowLayout by lazy { "com.google.accompanist:accompanist-flowlayout:${Versions.accompanist}" }
        val pager by lazy { "com.google.accompanist:accompanist-pager:${Versions.accompanist}" }
        val swipeRefresh by lazy { "com.google.accompanist:accompanist-swiperefresh:${Versions.accompanist}" }
        val placeholder by lazy { "com.google.accompanist:accompanist-placeholder:${Versions.accompanist}" }
        val pagerIndicators by lazy { "com.google.accompanist:accompanist-pager-indicators:${Versions.accompanist}" }
        val permissions by lazy { "com.google.accompanist:accompanist-permissions:${Versions.accompanist}" }
    }

    object Hilt {
        val android by lazy { "com.google.dagger:hilt-android:${Versions.hilt}" }
        val navigationCompose by lazy { "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}" }
        val compiler by lazy { "com.google.dagger:hilt-compiler:${Versions.hilt}" }
        val androidTesting by lazy { "com.google.dagger:hilt-android-testing:${Versions.hiltAndroidTesting}" }
        val androidCompiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.hiltAndroidTesting}" }
    }

    object MlKit {
        val textRecognition by lazy {
            "com.google.mlkit:text-recognition:${Versions.mlKitTextRecognition}"
        }
    }

    object Moshi {
        val moshi by lazy { "com.squareup.moshi:moshi:${Versions.moshi}" }
        val kotlinCodegen by lazy { "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}" }
    }

    object Retrofit {
        val retrofit by lazy { "com.squareup.retrofit2:retrofit:${Versions.retrofit}" }
        val moshiConverter by lazy { "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}" }
    }

    object OkHttp {
        val okHttp by lazy { "com.squareup.okhttp3:okhttp:${Versions.okHttp}" }
        val loggingInterceptor by lazy { "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}" }
    }

    object Camera {
        val camera2 by lazy { "androidx.camera:camera-camera2:${Versions.camera}" }
        val cameraView by lazy { "androidx.camera:camera-view:${Versions.camera}" }
        val cameraLifecycle by lazy { "androidx.camera:camera-lifecycle:${Versions.cameraLifecycle}" }
    }

    object Firebase {
        val bom by lazy { "com.google.firebase:firebase-bom:${Versions.firebaseBom}" }
        val crashlytics by lazy { "com.google.firebase:firebase-crashlytics-ktx" }
        val analytics by lazy { "com.google.firebase:firebase-analytics-ktx" }
        val perf by lazy { "com.google.firebase:firebase-perf-ktx" }
    }

    object JUnit {
        val testExt by lazy { "androidx.test.ext:junit:${Versions.testExtJUnit}" }
        val jUnit by lazy { "junit:junit:${Versions.jUnit}" }
    }

    object Chucker {
        val chucker by lazy { "com.github.chuckerteam.chucker:library:${Versions.chucker}" }
        val noOp by lazy { "com.github.chuckerteam.chucker:library-no-op:${Versions.chucker}" }
    }
}