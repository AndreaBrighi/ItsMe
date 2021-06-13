package com.example.itsme

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.itsme.databinding.CardAppWidgetConfigureBinding
import com.example.itsme.db.BusinessCardWithElements
import com.example.itsme.ui.HomeFragment
import com.example.itsme.viewModel.ListViewModel

/**
 * The configuration screen for the [CardAppWidget] AppWidget.
 */
class CardAppWidgetConfigureActivity : AppCompatActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var binding: CardAppWidgetConfigureBinding

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        binding = CardAppWidgetConfigureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, HomeFragment(true), null)
        }

        //binding.addButton.setOnClickListener(onClickListener)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        binding.fabCheck.setOnClickListener{
            val context = this@CardAppWidgetConfigureActivity

            // When the button is clicked, store the string locally
            // val widgetText = appWidgetText.text.toString()
            //saveTitlePref(context, appWidgetId, widgetText)

            // It is the responsibility of the configuration activity to update the app widget
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val listViewModel: ListViewModel =
                ViewModelProvider((this as ViewModelStoreOwner?)!!).get(
                    ListViewModel::class.java
                )
            listViewModel.selected.observe(this, { card: BusinessCardWithElements ->
                saveIdPref(context, appWidgetId,card.card.uidC)
            })
            updateAppWidget(context, appWidgetManager, appWidgetId)

            // Make sure we pass back the original appWidgetId
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(RESULT_OK, resultValue)
            finish()
        }

        //appWidgetText.setText(loadTitlePref(this@CardAppWidgetConfigureActivity, appWidgetId))
    }

}

private const val PREFS_NAME = "com.example.itsme.CardAppWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveIdPref(context: Context, appWidgetId: Int, int: Long) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putLong(PREF_PREFIX_KEY + appWidgetId, int)
    prefs.apply()
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadIdPref(context: Context, appWidgetId: Int): Long {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getLong(PREF_PREFIX_KEY + appWidgetId, 0)
}

internal fun deleteIdPref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}