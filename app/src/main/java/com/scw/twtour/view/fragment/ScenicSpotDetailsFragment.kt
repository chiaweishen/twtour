package com.scw.twtour.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.scw.twtour.MainActivity
import com.scw.twtour.databinding.FragmentScenicSpotDetailsBinding
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.view.viewmodel.ScenicSpotDetailsViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
class ScenicSpotDetailsFragment : Fragment() {

    private val args by navArgs<ScenicSpotDetailsFragmentArgs>()
    private val viewModel by viewModel<ScenicSpotDetailsViewModel>()

    private var _viewBinding: FragmentScenicSpotDetailsBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainActivity).setActionBarTitle(args.scenicSpotName)
    }

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

            displayTextView(textAddress, info.address)
            displayTextView(textTel, info.phone)
            displayTextView(textOpenTime, info.openTime, "開放時間:\n")
            displayTextView(textRemark, info.remarks, "注意事項:\n")
            displayTextView(textParking, info.parkingInfo)
            displayTextView(textTravelInfo, info.travelInfo, "旅遊資訊:\n")

            info.position?.also { position ->
                position.lat?.also { lat ->
                    position.lon?.also { lon ->
                        textAddress.setOnClickListener {
                            intentMap(info.name, lat, lon)
                        }

                        if (info.address.isNullOrBlank()) {
                            textAddress.visibility = View.VISIBLE
                            textAddress.text = "地圖"
                        }
                    }
                }
            }

            info.phone?.also { phone ->
                textTel.setOnClickListener {
                    intentPhoneDial(phone)
                }
            }

            info.parkingPosition?.also { position ->
                position.lat?.also { lat ->
                    position.lon?.also { lon ->
                        textParking.setOnClickListener {
                            intentMap("", lat, lon)
                        }

                        if (info.parkingInfo.isNullOrBlank()) {
                            textParking.visibility = View.VISIBLE
                            textParking.text = "停車資訊"
                        }
                    }
                }
            }

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

    private fun intentPhoneDial(phone: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null)))
    }

    private fun intentMap(name: String, lat: Double, lon: Double) {
        val uri = Uri.parse("geo:${lat},${lon}?q=${Uri.encode(name)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
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