package com.example.itsme.recyclerview.card

import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.DetailsFragment
import com.example.itsme.FindActivity
import com.example.itsme.R
import com.example.itsme.databinding.CardPreviewBinding
import java.io.*


class CardViewHolder(
    private val itemBinding: CardPreviewBinding,
    private val activity: FragmentActivity
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(card: Int) {
        itemBinding.nameView.text = card.toString() + " " + itemBinding.nameView.text

        itemBinding.detailsButton.setOnClickListener {
            activity.supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, DetailsFragment(), null)
                addToBackStack(this.javaClass.name)
            }
        }

        itemBinding.card.setOnClickListener {
            activity.supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, DetailsFragment(), null)
                addToBackStack(this.javaClass.name)
            }
        }

        itemBinding.sendButton.setOnClickListener {
            val intent = Intent(activity.baseContext, FindActivity::class.java)
            //intent.putExtra(EXTRA_ADDRESS, address)
            activity.startActivity(intent)
        }

        itemBinding.shareButton.setOnClickListener {
            val uriText: Uri? = saveFile()
            if (uriText != null) {

                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uriText)
                    type = "application/cxml"
                }
                activity.startActivity(Intent.createChooser(shareIntent, "Send"))
            }
        }
    }

    private fun saveFile(): Uri? {

        val name = "card.cxml"


        val contentUri = MediaStore.Files.getContentUri("external")

        val selection = MediaStore.MediaColumns.RELATIVE_PATH + "=?"

        val selectionArgs =
            arrayOf(Environment.DIRECTORY_DOCUMENTS + "/") //must include "/" in front and end


        val cursor: Cursor? =
            activity.contentResolver.query(contentUri, null, selection, selectionArgs, null)

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
                        "application/cxml"
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
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<note>test</note>"
                )
                out.close()

                outputStream?.close()
            } catch (e: IOException) {
                Toast.makeText(activity, "Fail to write file", Toast.LENGTH_SHORT)
                    .show()

            }

            cursor.close()
        }

        return uri
    }
}

