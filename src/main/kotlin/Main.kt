package com.example.codegenbuh

import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.SQLDataType
import org.postgresql.ds.PGSimpleDataSource
import org.testcontainers.containers.PostgreSQLContainer

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
        val select = ctx
            .select(DSL.field("id"), DSL.field("date"))
            .from("t_date")

        val rs = select.fetchResultSet()
        while (rs.next()) {
            val id = rs.getString("id")
            val date = rs.getDate("date")

            println("RS: id='$id', java.sql.Date='${date}', time=${date.time}, LocalDate='${date.toLocalDate()}'")
        }

        val id = DSL.field("id", SQLDataType.VARCHAR)
        val dateField = DSL.field("date", SQLDataType.DATE)

        select.fetch { record ->
            val id = record.get(id)
            val date = record.get(dateField)

            println("Record: id='$id', java.sql.Date='${date}', time=${date.time}, LocalDate='${date.toLocalDate()}'")
        }
    }
}
