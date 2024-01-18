package com.adityagupta.arcompanion.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.adityagupta.arcompanion.api.helpers.RetrofitHelper
import com.adityagupta.arcompanion.api.interfaces.Api
import com.adityagupta.arcompanion.databinding.FragmentMeaningBottomSheetBinding
import com.adityagupta.arcompanion.repositories.MeaningRepository
import com.adityagupta.arcompanion.viewmodels.MeaningActivityViewModel
import com.adityagupta.arcompanion.viewmodels.ViewModelFactory
import com.adityagupta.data.wordApi.WordApiDetailResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MeaningBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMeaningBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val meaningActivityViewModel: MeaningActivityViewModel by viewModels{
        ViewModelFactory(MeaningRepository(RetrofitHelper.getInstance().create(Api::class.java)))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMeaningBottomSheetBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("selectedWord")?.let { selectedWord ->
            meaningActivityViewModel.getOxfordWordMeaning(selectedWord)
            binding.selectedWordTextView.text = selectedWord
        }

        meaningActivityViewModel.wordApiDetails.observe(viewLifecycleOwner, ::handleWordApiDetailsResult)
    }


    private fun handleWordApiDetailsResult(definitions: WordApiDetailResponse?) {
        definitions?.let { result ->
            with(binding) {
                bottomSheetProgressBar.visibility = View.GONE
                selectedWordDefinitionTextView.visibility = View.VISIBLE
                selectedWordExampleTextView.visibility = View.VISIBLE
                selectedWordDefinitionTextView.text = result.results?.getOrNull(0)?.definition.orEmpty()
                selectedWordExampleTextView.text = result.results?.getOrNull(0)?.examples?.getOrNull(0).orEmpty()
            }
        } ?: run {
            Toast.makeText(requireContext(), "Something went wrong!! :(", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "MeaningBottomSheet"

        fun newInstance(selectedWord: String) = MeaningBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putString("selectedWord", selectedWord)
            }
        }
    }

}
