package com.raquo.app.basic

import com.raquo.app.codesnippets.CodeSnippets
import com.raquo.laminar.api.L.{*, given}

object CounterView {

  def apply(): HtmlElement = {
    div(
      cls("CounterView"),
      // BEGIN[counter]
      Counter(label = "Foo", initialStep = 1),
      // END[counter]
      CodeSnippets(_.`counter`)
    )
  }

  // BEGIN[counter]
  def Counter(label: String, initialStep: Int): HtmlElement = {
    val allowedSteps = List(1, 2, 3, 5, 10)
    val stepVar = Var(initialStep)
    val diffBus = new EventBus[Int]
    val countSignal: Signal[Int] = diffBus.events.scanLeft(initial = 0)(_ + _)

    div(
      cls("Counter"),
      p(
        "Step: ",
        select(
          value <-- stepVar.signal.map(_.toString),
          onChange.mapToValue.map(_.toInt) --> stepVar,
          allowedSteps.map { step => option(value := step.toString, step) }
        )
      ),
      p(
        label + ": ",
        b(text <-- countSignal),
        " ",
        // Two different ways to get stepVar's value:
        button("–", onClick.mapTo(-1 * stepVar.now()) --> diffBus),
        button("+", onClick(_.sample(stepVar.signal)) --> diffBus)
      )
    )
  }
  // END[counter]
}
