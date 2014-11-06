package com.spingo.graphviz

import org.scalatest.{Matchers, FunSpec}

class DigraphSpec extends FunSpec with Matchers{
  describe("generating a digraph") {
    it("does the thing") {
      import com.spingo.graphviz._
      val s = new java.io.StringWriter
      val dg = Digraph(s)
      dg.node("node", "number" -> 10, "float" -> 10.5, "xml" -> <a></a>, "string" -> "very string!")
      dg.close

      s.toString.trim should be ("""
      |digraph g{
      |  node [number=10 float=10.5 xml=<<a></a>> string="very string!"]
      |}
      |""".stripMargin.trim)
    }
  }
}
