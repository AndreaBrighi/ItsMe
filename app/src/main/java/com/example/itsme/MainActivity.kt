package com.example.itsme

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.itsme.databinding.ActivityMainBinding
import com.example.itsme.db.BusinessCardWithElements
import com.example.itsme.viewModel.ListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent?.extras != null) {
            if (intent?.extras!!.containsKey("uidC")) {
                val listViewModel = ViewModelProvider((this as ViewModelStoreOwner?)!!).get(
                    ListViewModel::class.java
                )
                listViewModel.getCardItemFromId(intent.getLongExtra("uidC", 0))?.observe(this,
                    { card: BusinessCardWithElements? ->
                        if (card != null) {
                            listViewModel.select(card)
                            supportFragmentManager.commit {
                                setReorderingAllowed(true)
                                replace(R.id.fragment_container_view, DetailsFragment(), null)
                                addToBackStack(this.javaClass.name)
                            }
                        }
                    })
            } else {
                if (intent?.extras!!.getString("destination") == "received") {
                    val intent = Intent(baseContext, ReceivedActivity::class.java)
                    startActivity(intent)
                } else if (intent?.extras!!.getString("destination") == "add") {
                    val intent = Intent(baseContext, AddActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }
}