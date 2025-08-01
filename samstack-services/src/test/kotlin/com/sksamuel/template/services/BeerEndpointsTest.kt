package com.sksamuel.template.services

import PooledR2dbcDataSource
import com.sksamuel.template.datastore.BeerDatastore
import com.sksamuel.template.datastore.flywayMigrate
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.testing.testApplication

class BeerEndpointsTest : FunSpec() {
   init {

      val ds = install(postgres) { flywayMigrate(this) }
      val mikromDs = PooledR2dbcDataSource(ds)
      val service = BeerService(mikromDs, BeerDatastore())

      test("Missing route should return 404") {
         testApplication {
            application {
               install(ContentNegotiation) { jackson() }
            }
            routing {
               beerEndpoints(service)
            }
            val resp = client.get("/v1/unkn")
            resp.status shouldBe HttpStatusCode.NotFound
         }
      }

      test("GET /v1/beer should return all beers") {
         testApplication {
            application {
               install(ContentNegotiation) { jackson() }
            }
            routing {
               beerEndpoints(service)
            }
            val resp = client.get("/v1/beer")
            resp.status shouldBe HttpStatusCode.OK
         }
      }
   }
}
