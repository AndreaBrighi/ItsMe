package com.example.itsme

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.itsme.db.BusinessCardRepository
import com.example.itsme.db.BusinessCardWithElements
import com.example.itsme.model.ElementTypes


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [CardAppWidgetConfigureActivity]
 */
class CardAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteIdPref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.card_app_widget)
    //views.setTextViewText(R.id.appwidget_text, widgetText)

    val res = context.resources

    val repository = BusinessCardRepository(context)
    val cardItem = loadIdPref(context, appWidgetId).let { repository.getCardItemFromId(it) }

    cardItem?.observe(ProcessLifecycleOwner.get(), { card: BusinessCardWithElements? ->

        card?.let {
            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
                .let { intent ->
                    intent.putExtra("uidC", it.card.uidC)
                    PendingIntent.getActivity(
                        context, 0, intent, 0
                    )
                }

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            views.apply {
                setOnClickPendingIntent(R.id.detailsButton, pendingIntent)

            }

            views.setTextViewText(
                R.id.appwidget_name,
                res.getString(R.string.name_par, card.card.firstName + " " + card.card.lastName)
            )
            views.setTextViewText(
                R.id.appwidget_type,
                res.getString(R.string.type_par, res.getString(card.card.types.stringResource))
            )

            val base = card.elements.groupBy { it.elementType }

            for (element in ElementTypes.values()) {
                when (element) {
                    ElementTypes.PHONE -> {
                        views.setViewVisibility(
                            R.id.phoneIcon,
                            if (base.containsKey(element)) View.VISIBLE else View.INVISIBLE
                        )
                    }
                    ElementTypes.MAIL -> {
                        views.setViewVisibility(
                            R.id.emailIcon,
                            if (base.containsKey(element)) View.VISIBLE else View.INVISIBLE
                        )
                    }
                    ElementTypes.WEB_PAGE -> {
                        views.setViewVisibility(
                            R.id.webIcon,
                            if (base.containsKey(element)) View.VISIBLE else View.INVISIBLE
                        )
                    }
                    ElementTypes.FACEBOOK -> {
                        views.setViewVisibility(
                            R.id.facebookIcon,
                            if (base.containsKey(element)) View.VISIBLE else View.INVISIBLE
                        )
                    }
                    ElementTypes.INSTAGRAM -> {
                        views.setViewVisibility(
                            R.id.instagramIcon,
                            if (base.containsKey(element)) View.VISIBLE else View.INVISIBLE
                        )
                    }
                }
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        } ?: run {
            views.setTextViewText(
                R.id.appwidget_name,
                res.getString(R.string.error)
            )
            views.setTextViewText(
                R.id.appwidget_type,
                res.getString(R.string.card_not_found)
            )
        }
    })


    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
