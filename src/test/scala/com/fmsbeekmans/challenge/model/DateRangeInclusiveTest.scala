package com.fmsbeekmans.challenge.model

import cats.Eq
import cats.syntax.eq.*
import cats.instances.list.*
import munit.FunSuite

import java.time.LocalDate

class DateRangeInclusiveTest extends FunSuite {
  test("Correctly chunks a period of less than 8 days") {
    val from = LocalDate.of(2024, 1, 1)
    val to = LocalDate.of(2024, 1, 1)
    val range = DateRangeInclusive.create(from, to)

    val chunks =
      range.chunk.compile.toList

    assert(chunks === List(range))
  }

  test("Correctly chunks a period of exactly 8 days") {
    val from = LocalDate.of(2024, 1, 1)
    val to = LocalDate.of(2024, 1, 8)
    val range = DateRangeInclusive.create(from, to)

    val chunks =
      range.chunk.compile.toList

    assert(chunks === List(range))
  }

  test("Correctly chunks a period of more than 8 days") {
    val from = LocalDate.of(2024, 1, 1)
    val to = LocalDate.of(2024, 1, 20)
    val range = DateRangeInclusive.create(from, to)

    val chunks =
      range.chunk.compile.toList

    val expected =
      List(
        DateRangeInclusive.create(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 8)),
        DateRangeInclusive.create(LocalDate.of(2024, 1, 9), LocalDate.of(2024, 1, 16)),
        DateRangeInclusive.create(LocalDate.of(2024, 1, 17), LocalDate.of(2024, 1, 20))
      )

    assert(chunks === expected)
  }
}
