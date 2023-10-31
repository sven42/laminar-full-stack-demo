package com.raquo.app

import com.raquo.app.JsRouter.*
import com.raquo.app.basic.*
import com.raquo.app.form.*
import com.raquo.app.integrations.{SapUI5WebComponentsView, ShoelaceWebComponentsView}
import com.raquo.app.todomvc.TodoMvcApp
import com.raquo.app.weather.WeatherGradientView
import com.raquo.laminar.api.L.{*, given}
import com.raquo.utils.JSImportSideEffect
import com.raquo.waypoint.*
import org.scalajs.dom
import vendor.highlightjs.hljs

object JsApp {

  // Load languages that we'll need for code snippets in Highlight.js
  hljs.registerLanguage(_.Scala, _.Scala)
  hljs.registerLanguage(_.Javascript, _.Javascript)
  hljs.registerLanguage(_.Less, _.Less)
  //hljs.registerLanguage(_.Css, _.Css)
  //hljs.registerLanguage(_.Html, _.Html)

  // Find and import the LESS (CSS) file for this component. See globResolverPlugin and importSideEffectPlugin
  JSImportSideEffect("@find/**/JsApp.less")

  // This method is the entry point of your JS app.
  // It is recognized by its name and type signature,
  // do not rename it.
  def main(args: Array[String]): Unit = {
    // Scala.js outputs to the browser dev console, not the sbt session
    // Always have the browser dev console open when developing web UIs.
    println("-- Scala.js app start --")

    // Find the div to render the app into. It's defined in index.html
    lazy val container = dom.document.getElementById("root")

    lazy val appElement = {
      div(
        cls := "JsApp",
        div(
          cls := "-content",
          child.maybe <-- JsRouter.currentPageSignal.map {
            case HomePage => None
            case _ => Some(h3(a(navigateTo(HomePage), "Back to home")))
          },
          // #Exercise for advanced readers: JsRouter.currentPageSignal emits
          // very rarely (only when user navigates to another page). However,
          // imagine if it was emitting various pages 1000 times per second.
          // Your task: learn about the `split` operator to understand what
          // is inefficient about this .map in such a scenario, and fix the
          // inefficiency using the `splitOne` version of that operator.
          child <-- JsRouter.currentPageSignal.map {
            case page: TitledPage => h1(page.title)
            case _ => emptyNode
          },
          child <-- selectedAppSignal
        )
      )
    }

    render(container, appElement)
  }

  private val selectedAppSignal = SplitRender(JsRouter.currentPageSignal)
    .collectStatic(HomePage)(HomePageView())
    .collectStatic(HelloWorldPage)(HelloWorldView())
    .collectStatic(CounterPage)(CounterView())
    .collectStatic(TimePage)(TimeView())
    .collectStatic(UncontrolledInputsPage)(UncontrolledInputsView())
    .collectStatic(ControlledInputsPage)(ControlledInputsView())
    .collectStatic(FormStatePage)(FormStateView())
    .collectStatic(TodoMvcPage)(TodoMvcApp.node)
    .collectSignal[WeatherGradientPage](WeatherGradientView(_))
    .collectStatic(UI5WebComponentsPage)(SapUI5WebComponentsView())
    .collectStatic(ShoelaceWebComponentsPage)(ShoelaceWebComponentsView())
    .collectStatic(NotFoundPage)(renderNotFoundPage())
    .signal

  // All the other page Views are defined in different files
  // for easier organization, but of course you can just use
  // plain functions like this too.
  private def renderNotFoundPage(): HtmlElement = {
    div(
      p("The Waypoint frontend router could not match this URL to any of the routes, so it is rendering the fallback page (NotFoundPage) instead."),
      p("OR – maybe you directly asked Waypoint to render NotFoundPage, e.g. if the URL format was correct but the provided params in the URL were invalid."),
      p("The important part being, it's not the server giving you a 404. The server loaded index.html and that loaded your frontend code, and this frontend code is what's showing this page.")
    )
  }
}