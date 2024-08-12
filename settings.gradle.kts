pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/shreyas-android/GeminiEnhancedAPI")
            credentials {
                username =   System.getenv("GPR_USER")
                password = System.getenv("GPR_API_KEY")
            }
        }
    }
}

rootProject.name = "AppaAIProject"

include(":app")
include(":feature:appai")
include(":utils")
include(":ui")
include(":core")
include(":core:fatherai")
