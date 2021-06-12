package com.example.itsme.recyclerview.card

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.DetailsFragment
import com.example.itsme.FindActivity
import com.example.itsme.R
import com.example.itsme.databinding.CardPreviewBinding
import com.example.itsme.db.BusinessCardWithElements
import com.example.itsme.model.ElementTypes
import com.example.itsme.model.Utilities
import com.example.itsme.viewModel.ListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class CardViewHolder(
    private val itemBinding: CardPreviewBinding,
    private val activity: FragmentActivity,
    private val config: Boolean
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(card: BusinessCardWithElements) {
        itemBinding.nameView.text = activity.resources.getString(
            R.string.name_par,
            card.card.firstName + " " + card.card.lastName
        )
        itemBinding.typeView.text = activity.resources.getString(
            R.string.type_par,
            activity.resources.getString(card.card.types.stringResource)
        )
        if (config) {
            itemBinding.sendButton.visibility = View.INVISIBLE
            itemBinding.shareButton.visibility = View.INVISIBLE
        }

        val listener = View.OnClickListener {
            val listViewModel = ViewModelProvider((activity as ViewModelStoreOwner?)!!).get(
                ListViewModel::class.java
            )
            listViewModel.select(card)
            activity.supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, DetailsFragment(config), null)
                addToBackStack(this.javaClass.name)
            }
        }

        itemBinding.deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(activity)
                .setTitle("Delete")
                .setMessage("Do you want to delete this card?")
                .setNegativeButton("No") { di: DialogInterface, _: Int ->
                    di.dismiss()
                }
                .setPositiveButton("Delete") { _: DialogInterface, _: Int ->
                    ViewModelProvider((activity as ViewModelStoreOwner?)!!).get(
                        ListViewModel::class.java
                    ).deleteCard(card)
                }
                .show()
        }

        itemBinding.detailsButton.setOnClickListener(listener)

        itemBinding.card.setOnClickListener(listener)

        itemBinding.sendButton.setOnClickListener {
            val listViewModel = ViewModelProvider((activity as ViewModelStoreOwner?)!!).get(
                ListViewModel::class.java
            )
            listViewModel.select(card)
            val intent = Intent(activity.baseContext, FindActivity::class.java)
            activity.startActivity(intent)
        }

        itemBinding.shareButton.setOnClickListener {
            val uriText: Uri? = Utilities.saveFile(activity, card)
            if (uriText != null) {

                val shareIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uriText)
                    type = "application/pdf"
                }
                activity.startActivity(Intent.createChooser(shareIntent, "Send"))
            }

        }

        val base = card.elements.groupBy { it.elementType }

        for (element in ElementTypes.values()) {
            when (element) {
                ElementTypes.PHONE -> {
                    itemBinding.phoneIcon.visibility =
                        if (base.containsKey(element)) View.VISIBLE else View.INVISIBLE
                }
                ElementTypes.MAIL -> {
                    itemBinding.emailIcon.visibility =
                        if (base.containsKey(element)) View.VISIBLE else View.INVISIBLE
                }
                ElementTypes.WEB_PAGE -> {
                    itemBinding.webIcon.visibility =
                        if (base.containsKey(element)) View.VISIBLE else View.INVISIBLE
                }
                ElementTypes.FACEBOOK -> {
                    itemBinding.facebookIcon.visibility =
                        if (base.containsKey(element)) View.VISIBLE else View.INVISIBLE
                }
                ElementTypes.INSTAGRAM -> {
                    itemBinding.instagramIcon.visibility =
                        if (base.containsKey(element)) View.VISIBLE else View.INVISIBLE
                }
            }

        }
    }

}

