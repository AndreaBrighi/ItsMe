package com.example.itsme

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.itsme.databinding.FragmentDialogBinding








/**
 * A simple [Fragment] subclass.
 */
class EditDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogBinding

    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(title: String?, input: Int, value: String =""): EditDialogFragment {
            val frag = EditDialogFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("default", value)
            args.putInt("input", input)
            frag.arguments = args
            return frag
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view
        // Fetch arguments from bundle and set title
        val title = requireArguments().getString("title", "field")
        binding.help.text = resources.getString(R.string.insert_par, title)
        // Show soft keyboard automatically and request focus to field
        binding.editName.requestFocus()
        dialog!!.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )

        binding.editTextView.setText(requireArguments().getString("default", ""))
        binding.editTextView.inputType = requireArguments().getInt("input")

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.insertButton.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("value",binding.editTextView.text.toString())
            setFragmentResult("requestKey", bundle)
            dismiss()
        }
    }

    override fun onResume() {
        // Store access variables for window and blank point
        val window = dialog!!.window!!
        // Store dimensions of the screen in `size`
        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = windowManager.currentWindowMetrics.bounds.width()
        //val height = windowManager.currentWindowMetrics.bounds.height()
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((width * 0.75).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        // Call super onResume after sizing
        super.onResume()
    }
}