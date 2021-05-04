package com.example.itsme

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlSerializer
import java.io.StringReader
import java.io.StringWriter

class XMLManager {

    fun writeXML() {
        val xmlSerializer = Xml.newSerializer()
        val xmlString = xmlSerializer.writeDocument {
            writeElement("Movies") {
                for (i in 1..3) {
                    writeElement("row") {
                        writeAttribute("no", i.toString())
                        writeElement("FL", "6000000066015") {
                            writeAttribute("val", "TicketId")
                        }
                        writeElement("FL", "Dunkirk") {
                            writeAttribute("val", "MovieName")
                        }
                        writeElement("FL") {
                            writeAttribute("val", "TimeLog")
                            writeElement("row") {
                                writeAttribute("no", "1")
                                writeElement("FL", "23/01/2018") {
                                    writeAttribute("val", "date")
                                }
                                writeElement("FL", "08:00") {
                                    writeAttribute("val", "startTime")
                                }
                            }
                        }
                    }
                }
            }
        }
        Log.v("Tes", xmlString)
    }

    fun readXML() {
        val xmlString: XmlPullParser = Xml.newPullParser()
        xmlString.readDocument(
            "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>" +
                    "<Movies><row no=\"1\">t</row>" +
                    "</Movies>"
        ) {

        }
        //Log.v("Tes", xmlString)
    }

    private fun XmlSerializer.writeDocument(
        docName: String = "UTF-8",
        xmlStringWriter: StringWriter = StringWriter(),
        init: XmlSerializer.() -> Unit
    ): String {
        startDocument(docName, true)
        xmlStringWriter.buffer.setLength(0) //  refreshing string writer due to reuse
        setOutput(xmlStringWriter)
        init()
        endDocument()
        return xmlStringWriter.toString()
    }

    //  element
    private fun XmlSerializer.writeElement(name: String, init: XmlSerializer.() -> Unit) {
        startTag("", name)
        init()
        endTag("", name)
    }

    //  element with attribute & content
    private fun XmlSerializer.writeElement(
        name: String,
        content: String,
        init: XmlSerializer.() -> Unit
    ) {
        startTag("", name)
        init()
        text(content)
        endTag("", name)
    }

    //  element with content
    private fun XmlSerializer.writeElement(name: String, content: String) =
        writeElement(name) {
            text(content)
        }

    //  attribute
    private fun XmlSerializer.writeAttribute(name: String, value: String) =
        attribute("", name, value)


    private fun XmlPullParser.readDocument(
        string: String,
        init: XmlPullParser.() -> Unit
    ) {
        val xmlStringReader = StringReader(string)
        xmlStringReader.buffered(DEFAULT_BUFFER_SIZE)
        setInput(xmlStringReader)
        var eventType: Int = eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_DOCUMENT ->
                    Log.v("Tes", "Start document")
                XmlPullParser.START_TAG -> {
                    Log.v("Tes", "Start tag $name")
                    for (i in 0 until attributeCount) {
                        Log.v("Tes", "attribute name tag ${getAttributeName(i)}")
                        Log.v("Tes", "attribute value tag ${getAttributeValue(i)}")
                    }
                    init()
                }
                XmlPullParser.END_TAG ->
                    Log.v("Tes", "End tag $name")
                XmlPullParser.TEXT ->
                    Log.v("Tes", "Text $text")
            }
            eventType = next()
        }
        Log.v("Tes", "End document")
        //return xmlStringWriter.toString()
    }

    //  element
    private fun XmlPullParser.readElement(name: String, init: XmlPullParser.() -> Unit) {
        if (name == this.name) {
            var eventType: Int = eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        Log.v("Tes", "Start tag $name")
                        for (i in 0 until attributeCount) {
                            Log.v("Tes", "attribute name tag ${getAttributeName(i)}")
                            Log.v("Tes", "attribute value tag ${getAttributeValue(i)}")
                        }
                        init()
                    }
                    XmlPullParser.END_TAG ->
                        Log.v("Tes", "End tag $name")
                    XmlPullParser.TEXT ->
                        Log.v("Tes", "Text $text")
                }
                eventType = next()
            }
        }
    }

//    //  element with attribute & content
//    private fun XmlSerializer.readElement(
//        name: String,
//        content: String,
//        init: XmlSerializer.() -> Unit
//    ) {
//        startTag("", name)
//        init()
//        text(content)
//        endTag("", name)
//    }
//
//    //  element with content
//    private fun XmlPullParser.readElement(name: String, content: String) =
//        readElement(name) {
//            text(content)
//        }
//
//    //  attribute
//    private fun XmlPullParser.readAttribute(name: String, value: String) =
//        attribute("", name, value)
}