import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.text.SimpleDateFormat
import java.util.*


fun getApiKey(): String {
    return gradleLocalProperties(rootDir).getProperty("TMDB_API_KEY")
}

class ApplicationVariantOutputFilenameAction : Action<ApplicationVariant> {
    override fun execute(variant: ApplicationVariant) {
        val fileName = getVariantFileOutputName(variant)
        variant.outputs.all(VariantOutputAction(fileName))
    }

    class VariantOutputAction(
        private val fileName: String
    ) : Action<com.android.build.gradle.api.BaseVariantOutput> {
        override fun execute(output: com.android.build.gradle.api.BaseVariantOutput) {
            if (output is BaseVariantOutputImpl) {
                output.outputFileName = fileName
            }
        }
    }

    private fun getVariantFileOutputName(variant: ApplicationVariant): String {
        val currentDate = SimpleDateFormat("ddMMyyyy_HHmm").run {
            format(Date())
        }

        return listOfNotNull(
            "app",
            variant.buildType.name,
            variant.versionName,
            currentDate
        ).joinToString(separator = "_", postfix = ".apk")
    }
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version Versions.devtoolsVersion
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("com.google.gms.google-services")
    id("io.gitlab.arturbosch.detekt")
}

android {
    namespace = BuildConfig.namespace
    compileSdk = BuildConfig.compileSdkVersion

    defaultConfig {
        applicationId = BuildConfig.applicationId
        minSdk = BuildConfig.minSdkVersion
        targetSdk = BuildConfig.targetSdkVersion
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName

        buildConfigField("String", "TMDB_API_KEY", getApiKey())

        testInstrumentationRunner = "com.example.moviesapp.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.putAll(
                    mutableMapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    applicationVariants.all(ApplicationVariantOutputFilenameAction())

    testOptions {
        animationsDisabled = true
        unitTests {
            isReturnDefaultValues = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }

    packagingOptions {
        resources {
            excludes.addAll(
                listOf(
                    "/META-INF/{AL2.0,LGPL2.1}",
                    "**/attach_hotspot_windows.dll",
                    "META-INF/licenses/**",
                    "META-INF/AL2.0",
                    "META-INF/LGPL2.1"
                )
            )
        }
        jniLibs {
            excludes.add("ETA-INF/licenses/**")
        }
    }
}

kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

dependencies {
    implementation(Dependencies.pagingCompose)

    //Compose Destinations
    implementation(Dependencies.ComposeDestinations.animationCore)
    ksp(Dependencies.ComposeDestinations.ksp)

    //Room
    implementation(Dependencies.Room.ktx)
    implementation(Dependencies.Room.paging)
    kapt(Dependencies.Room.compiler)

    //Compose
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.constraintLayout)
    implementation(Dependencies.Compose.activity)
    implementation(Dependencies.Compose.lottie)
    implementation(Dependencies.Compose.uiTooling)

    //Accompanist
    implementation(Dependencies.Accompanist.systemUiController)
    implementation(Dependencies.Accompanist.flowLayout)
    implementation(Dependencies.Accompanist.pager)
    implementation(Dependencies.Accompanist.pagerIndicators)
    implementation(Dependencies.Accompanist.placeholder)
    implementation(Dependencies.Accompanist.swipeRefresh)
    implementation(Dependencies.Accompanist.permissions)

    //Hilt
    implementation(Dependencies.Hilt.android)
    implementation(Dependencies.Hilt.navigationCompose)
    kapt(Dependencies.Hilt.compiler)

    //MLKit
    implementation(Dependencies.MlKit.textRecognition)

    //Moshi
    implementation(Dependencies.Moshi.moshi)
    kapt(Dependencies.Moshi.kotlinCodegen)

    //Retrofit
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.Retrofit.moshiConverter)

    //OkHttp
    implementation(Dependencies.OkHttp.okHttp)
    implementation(Dependencies.OkHttp.loggingInterceptor)

    //Camera
    implementation(Dependencies.Camera.camera2)
    implementation(Dependencies.Camera.cameraView)
    implementation(Dependencies.Camera.cameraLifecycle)

    //Firebase
    implementation(project.dependencies.platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.crashlytics)
    implementation(Dependencies.Firebase.analytics)
    implementation(Dependencies.Firebase.perf)

    implementation(Dependencies.coilCompose)
    implementation(Dependencies.palette)
    implementation(Dependencies.splashScreen)
    implementation(Dependencies.core)
    implementation(Dependencies.lifecycleRuntime)
    implementation(Dependencies.timber)

    //Unit Test
    testImplementation(Dependencies.coroutinesTest)
    testImplementation(Dependencies.truth)
    testImplementation(Dependencies.coreTesting)
    testImplementation(Dependencies.Hilt.androidTesting)
    testImplementation(Dependencies.mockito)
    testImplementation(Dependencies.JUnit.testExt)
    testImplementation(Dependencies.JUnit.jUnit)
    kaptTest(Dependencies.Hilt.androidCompiler)

    //Instrumented Unit Tests
    androidTestImplementation(Dependencies.mockWebServer)
    androidTestImplementation(Dependencies.JUnit.testExt)
    androidTestImplementation(Dependencies.JUnit.jUnit)
    androidTestImplementation(Dependencies.coroutinesTest)
    androidTestImplementation(Dependencies.coreTesting)
    androidTestImplementation(Dependencies.truth)
    androidTestImplementation(Dependencies.espresso)
    androidTestImplementation(Dependencies.mockito)
    androidTestImplementation(Dependencies.Hilt.androidTesting)
    kaptAndroidTest(Dependencies.Hilt.androidCompiler)
    androidTestImplementation(Dependencies.Compose.uiTest)

    debugImplementation(Dependencies.Compose.uiTooling)
    debugImplementation(Dependencies.leakCanary)
    debugImplementation(Dependencies.Chucker.chucker)

    releaseImplementation(Dependencies.Chucker.noOp)
}