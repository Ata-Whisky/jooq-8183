package com.example.codegenbuh

import java.sql.Date
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import org.testcontainers.containers.PostgreSQLContainer
import java.time.LocalDate
import org.jooq.impl.DefaultConfiguration
import org.postgresql.ds.PGSimpleDataSource

fun main() {
    val postgres = PostgreSQLContainer<Nothing>("postgres:17-alpine")
        .apply {
            withStartupTimeout(java.time.Duration.ofSeconds(120))
            withInitScript("init_db.sql")
            start()
        }

    val dataSource = PGSimpleDataSource().apply {
        setURL(postgres.jdbcUrl)
        user = postgres.username
        password = postgres.password
    }

    val config = DefaultConfiguration()
        .set(dataSource)
        .set(SQLDialect.POSTGRES)

    val rootDsl = DSL.using(config)

    rootDsl.transaction { c ->
        val ctx = c.dsl()

        val id = DSL.field("id", SQLDataType.VARCHAR)
        val localDateField = DSL.field("local_date", SQLDataType.LOCALDATE)
        val dateField = DSL.field("date", SQLDataType.DATE)

        val records = ctx
            .select(id, dateField, localDateField)
            .from("t_date")
            .fetch { record ->

                DateRecord(
                    id = record.get(id),
                    localDate = record.get(localDateField),
                    date = record.get(dateField),
                )
            }

        val positiveLocalDate = records.first { it.id == "positive" }
        println("Positive date expected to be '2024-12-30' is : LocalDate='${positiveLocalDate.localDate}', java.sql.Date='${positiveLocalDate.date}'")
        val negativeDate = records.first { it.id == "negative" }
        println("Negative date expected to be '-2025-12-30' is : java.sql.Date='${negativeDate.localDate}' or java.sql.Date='${negativeDate.date}'")
    }
}

data class DateRecord(
    val id: String,
    val localDate: LocalDate,
    val date: Date,
)
