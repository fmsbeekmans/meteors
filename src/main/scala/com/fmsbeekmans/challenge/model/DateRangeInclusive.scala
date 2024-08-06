package com.fmsbeekmans.challenge.model

import cats.{ Eq, Order }
import cats.syntax.order.*
import fs2.Stream

import java.time.LocalDate

object DateRangeInclusive:

  given Order[LocalDate] = Order.by(_.toEpochDay)

  given Eq[DateRangeInclusive] = Eq.by { range => range.from -> range.to }

  def create(
    from: LocalDate,
    to: LocalDate
  ): DateRangeInclusive =
    new DateRangeInclusive(
      from.min(to),
      from.max(to)
    )

case class DateRangeInclusive private (
  from: LocalDate,
  to: LocalDate
):

  import DateRangeInclusive.given

  def chunk: Stream[fs2.Pure, DateRangeInclusive] = {
    Stream
      .unfold(from) {
        case intervalStart if intervalStart > to => None
        case intervalStart =>
          val intervalEnd =
            intervalStart
              .plusDays(7)
              .min(to)

          Some(
            (
              new DateRangeInclusive(intervalStart, intervalEnd),
              intervalEnd.plusDays(1)
            )
          )
      }
  }
