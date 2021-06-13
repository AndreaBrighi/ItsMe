package com.example.itsme.model

import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Xml
import android.widget.Toast
import com.example.itsme.R
import com.example.itsme.db.BusinessCard
import com.example.itsme.db.BusinessCardElement
import com.example.itsme.db.BusinessCardWithElements
import org.xmlpull.v1.XmlPullParser
import java.io.*

object Utilities {

    fun saveFile(activity: Activity, card: BusinessCardWithElements): Uri? {

        val name = "card.xml"


        val contentUri = MediaStore.Files.getContentUri("external")

        val selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?"

        val selectionArgs =
            arrayOf(Environment.DIRECTORY_DOCUMENTS + "/") //must include "/" in front and end


        val cursor: Cursor? =
            activity.contentResolver.query(
                contentUri,
                null,
                selection,
                selectionArgs,
                null
            )

        var uri: Uri? = null

        if (cursor != null) {
            if (cursor.count != 0) {

                while (cursor.moveToNext()) {
                    val fileName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                    if (fileName == name) {                          //must include extension
                        val id =
                            cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
                        uri = ContentUris.withAppendedId(contentUri, id)
                        break
                    }
                }
            }
            try {
                if (uri == null) {
                    val values = ContentValues()

                    values.put(
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        name
                    )       //file name
                    values.put(
                        MediaStore.MediaColumns.MIME_TYPE,
                        "application/xml"
                    )        //file extension, will automatically add to file
                    values.put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_DOCUMENTS + "/"
                    )     //end "/" is not mandatory

                    uri = activity.contentResolver.insert(
                        MediaStore.Files.getContentUri("external"),
                        values
                    )      //important!
                }
                val outputStream: OutputStream? = uri.let {
                    activity.contentResolver.openOutputStream(it!!, "rwt")
                }

                val out: Writer = BufferedWriter(OutputStreamWriter(outputStream))
                out.write(
                    card.getXMLString()
                )
                out.close()

                outputStream?.close()
            } catch (e: IOException) {
                Toast.makeText(activity, activity.resources.getString(R.string.fail_file), Toast.LENGTH_SHORT)
                    .show()

            }

            cursor.close()
        }

        return uri
    }

    fun readXMLString(string: String): BusinessCardWithElements {
        val xmlString: XmlPullParser = Xml.newPullParser()
        val map = xmlString.readDocument(string)
        val card = BusinessCardWithElements(
            BusinessCard(
                0,
                map["firstName"]!![0],
                map["lastName"]!![0],
                CardTypes.SOCIAL,
                true
            ), ArrayList()
        )

        for (elem in ElementTypes.values()){
            if(map.containsKey(elem.toString().lowercase())) {
                for (value in map[elem.toString().lowercase()]!!) {
                    card.elements.add(BusinessCardElement(0, elem, value))
                }
            }
        }
        return card
    }
}