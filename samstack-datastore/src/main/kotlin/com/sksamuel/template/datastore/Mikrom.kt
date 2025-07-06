package com.sksamuel.template.datastore

import com.github.kantis.mikrom.Mikrom
import com.sksamuel.template.myservice.domain.Beer
import com.sksamuel.template.myservice.domain.BeerName
import com.sksamuel.template.myservice.domain.BeerType
import com.sksamuel.template.myservice.domain.Iso3Country

val mikrom = Mikrom {
   registerRowMapper {  row ->
      Beer(
         name = BeerName(row["name"] as String),
         type = BeerType.valueOf(row["type"] as String),
         country = Iso3Country(row["country"] as String),
      )
   }
}
