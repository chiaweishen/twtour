package com.scw.twtour.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.scw.twtour.databinding.FragmentScenicSpotDetailsBinding
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.view.viewmodel.ScenicSpotDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScenicSpotDetailsFragment : Fragment() {

    private val args by navArgs<ScenicSpotDetailsFragmentArgs>()
    private val viewModel by viewModel<ScenicSpotDetailsViewModel>()

    private var _viewBinding: FragmentScenicSpotDetailsBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onStart() {
        super.onStart()
        viewModel.fetchScenicSpotItems(args.scenicSpotId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentScenicSpotDetailsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun collectData() {
        viewModel.scenicSpotInfo.observe(viewLifecycleOwner) {
            updateView(it)
        }
    }

    private fun updateView(info: ScenicSpotInfo) {
        with(viewBinding) {
            viewImage.load(info.pictures.firstOrNull())
            textTitle.text = info.name

            displayTextView(textAddress, info.address, "地址:\n")
            displayTextView(textTel, info.phone, "電話:\n")
            displayTextView(textOpenTime, info.openTime, "開放時間:\n")
            displayTextView(textRemark, info.remarks, "注意事項:\n")
            displayTextView(textParking, info.parkingInfo, "停車資訊:\n")
            displayTextView(textTravelInfo, info.travelInfo, "旅遊資訊:\n")

            if (info.descriptionDetail?.isNotBlank() == true) {
                textDescription.text = info.descriptionDetail
                textDescription.visibility = View.VISIBLE
            } else if (info.description?.isNotBlank() == true) {
                textDescription.text = info.description
                textDescription.visibility = View.VISIBLE
            } else {
                textDescription.visibility = View.GONE
            }
        }
    }

    private fun displayTextView(view: TextView, text: String?, prefix: String = "") {
        if (text?.isNotBlank() == true) {
            view.text = prefix + text
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}