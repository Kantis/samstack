package com.sksamuel.template.datastore

import com.github.kantis.mikrom.Query
import com.github.kantis.mikrom.datasource.SuspendingTransaction
import com.github.kantis.mikrom.execute
import com.github.kantis.mikrom.queryFor
import com.sksamuel.template.myservice.domain.Beer
import kotlinx.coroutines.flow.Flow

class BeerDatastore() {
   context(SuspendingTransaction)
   suspend fun insert(beer: Beer) {
      mikrom.execute(
         Query("INSERT INTO beers (name, type, country) VALUES (?,?,?)"),
         listOf(beer.name.value, beer.type.name, beer.country.value)
      )
   }

   context(SuspendingTransaction)
   suspend fun findAll(): Flow<Beer> {
      return mikrom.queryFor<Beer>(Query("SELECT * FROM beers"))
   }
}
