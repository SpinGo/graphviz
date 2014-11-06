package com.spingo.graphviz

object XmlSupport {
  case class GvXml(value: scala.xml.Elem) extends GvValue {
    def render = "<" + value.toString + ">"
  }
  implicit def xmlToGvXml(n: scala.xml.Elem) = GvXml(n)
}
