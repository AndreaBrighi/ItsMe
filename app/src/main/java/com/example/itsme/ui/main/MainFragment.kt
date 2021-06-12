package com.example.itsme.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.itsme.R
import com.example.itsme.databinding.FragmentMainBinding
import com.example.itsme.db.BusinessCardWithElements
import com.example.itsme.model.CardTypes
import com.example.itsme.recyclerview.card.CardAdapter
import com.example.itsme.viewModel.ListViewModel
import java.util.function.Predicate

/**
 * A placeholder fragment containing a simple view.
 */
class MainFragment(private val config: Boolean = false) : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var _binding: FragmentMainBinding? = null
    private var listViewModel: ListViewModel? = null
    private var position = 1
    private var predicatePage = Predicate<BusinessCardWithElements> { true }
    private var predicateFilter = Predicate<BusinessCardWithElements> { true }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
            position = arguments?.getInt(ARG_SECTION_NUMBER) ?: 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        val spinner: AppCompatSpinner = binding.spinnerFilter
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val recyclerView: RecyclerView = binding.cardRecyclerView
        recyclerView.setHasFixedSize(true)
        val adapter = CardAdapter(this.requireActivity(), config)




        recyclerView.adapter = adapter
        listViewModel = ViewModelProvider((activity as ViewModelStoreOwner?)!!).get(
            ListViewModel::class.java
        )

        when (binding.spinnerFilter.selectedItemPosition) {
            0 -> {
                predicateFilter = Predicate<BusinessCardWithElements> { true }
            }
            2 -> {
                predicateFilter =
                    Predicate<BusinessCardWithElements> { it.card.types == CardTypes.WORK }
            }
            1 -> {
                predicateFilter =
                    Predicate<BusinessCardWithElements> { it.card.types == CardTypes.SOCIAL }
            }
            else -> {
            }
        }




        when (position) {
            1 -> {
                predicatePage = Predicate<BusinessCardWithElements> { !it.card.received }
            }
            2 -> {
                predicatePage = Predicate<BusinessCardWithElements> { it.card.received }
            }
            else -> {
            }
        }

        listViewModel!!.getCardItems()
            .observe(activity as LifecycleOwner) { cardItems ->
                if (cardItems != null) {
                    adapter.setData(
                        cardItems.filter { predicatePage.test(it) && predicateFilter.test(it) }.distinct()
                    )
                }
            }

        binding.spinnerFilter.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        predicateFilter = Predicate<BusinessCardWithElements> { true }
                    }
                    2 -> {
                        predicateFilter =
                            Predicate<BusinessCardWithElements> { it.card.types == CardTypes.WORK }
                    }
                    1 -> {
                        predicateFilter =
                            Predicate<BusinessCardWithElements> { it.card.types == CardTypes.SOCIAL }
                    }
                    else -> {
                    }

                }
                listViewModel!!.getCardItems()
                    .observe(activity as LifecycleOwner) { cardItems ->
                        if (cardItems != null) {
                            adapter.setData(
                                cardItems.filter { predicatePage.test(it) && predicateFilter.test(it) }.distinct()
                            )
                        }
                    }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

//        arguments?.takeIf { it.containsKey(ARG_SECTION_NUMBER) }?.apply {
//            when( getInt(ARG_SECTION_NUMBER)) {
//                1 ->
//            }
//        }


        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int, config: Boolean): MainFragment {
            return MainFragment(config).apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}