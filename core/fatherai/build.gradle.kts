import org.jetbrains.kotlin.js.translate.context.Namer.kotlin

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed plugins {
plugins{
    kotlin("multiplatform")
    alias(libs.plugins.androidLibrary)
    id("app.cash.sqldelight") version "2.0.1"
}

kotlin {

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "fatherai"
        }
    }

    sourceSets {
        val sqlDelightVersion = "2.0.1"
        val dateTimeVersion = "0.4.0"
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
                implementation(libs.atomicfu)
                implementation(libs.kotlin.coroutines.core)
                implementation("app.cash.sqldelight:primitive-adapters:$sqlDelightVersion")

                implementation("app.cash.sqldelight:runtime:$sqlDelightVersion")
                implementation("app.cash.sqldelight:coroutines-extensions:$sqlDelightVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeVersion")

                api("app.cash.sqldelight:androidx-paging3-extensions:2.0.0")
            }

        }
        val commonTest by getting {
            dependencies {
                // implementation(libs.kotlin.test)
                implementation("app.cash.sqldelight:runtime:$sqlDelightVersion")
            }
        }

        val androidMain by getting{
            dependencies {
               // implementation(libs.sqlDelight.android.driver)
                implementation("app.cash.sqldelight:android-driver:$sqlDelightVersion")

            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation("app.cash.sqldelight:native-driver:$sqlDelightVersion")

            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }


    }
}

android {
    namespace = "com.cogniheroid.framework.shared.core.fatherai"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}




sqldelight {
    databases {
        create("FatherAIDatabase"){
            packageName.set("com.cogniheroid.framework.shared.core.fatherai.database")
            srcDirs.setFrom("src/main/sqldelight")
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
//            schemaOutputDirectory.set(file("src/commonMain/db/databases"))
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
            migrationOutputDirectory.set(file("src/main/db/migrations"))
           // generateAsync.set(true)

        }
    }
}

task("testClasses").doLast {
    println("This is a dummy testClasses task")
}

