package com.sksamuel.template.services

import com.github.kantis.mikrom.datasource.SuspendingDataSource
import com.sksamuel.template.datastore.BeerDatastore
import com.sksamuel.template.myservice.domain.Beer
import com.sksamuel.template.myservice.domain.BeerName
import com.sksamuel.template.myservice.domain.BeerType
import com.sksamuel.template.myservice.domain.Iso3Country
import kotlinx.coroutines.flow.toList

class BeerService(
   private val dataSource: SuspendingDataSource,
   private val datastore: BeerDatastore
) {

   suspend fun brew(name: String, type: BeerType, country: Iso3Country): Result<Beer> =
      runCatching {
         dataSource.suspendingTransaction {
            Beer(BeerName(name), type, country).also {
               datastore.insert(it)
            }
         }
      }

   suspend fun all(): Result<List<Beer>> =
      runCatching {
         dataSource.suspendingTransaction { datastore.findAll().toList() }
      }
}
