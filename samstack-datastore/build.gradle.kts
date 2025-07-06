plugins {
   `java-test-fixtures`
}

kotlin {
   compilerOptions {
      freeCompilerArgs.add("-Xcontext-receivers")
   }
}

dependencies {
   api(projects.samstackDomain)
   api(libs.micrometer.core)
   api(libs.postgresql)
   api(libs.r2dbc.pool)
   api(libs.mikrom.r2dbc)
   api(libs.hikari)
   api(libs.flyway.core)
}
