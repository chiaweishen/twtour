package com.scw.twtour.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.scw.twtour.MainActivity
import com.scw.twtour.R
import com.scw.twtour.databinding.FragmentScenicSpotDetailsBinding
import com.scw.twtour.model.data.Result
import com.scw.twtour.model.data.ScenicSpotInfo
import com.scw.twtour.util.ErrorUtil
import com.scw.twtour.view.viewmodel.ScenicSpotDetailsViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
class ScenicSpotDetailsFragment : Fragment() {

    private val args by navArgs<ScenicSpotDetailsFragmentArgs>()
    private val viewModel by viewModel<ScenicSpotDetailsViewModel>()

    private var _viewBinding: FragmentScenicSpotDetailsBinding? = null
    private val viewBinding get() = _viewBinding!!

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

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).setActionBarTitle(args.scenicSpotName)
        viewModel.fetchScenicSpotItems(args.scenicSpotId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun collectData() {
        viewModel.scenicSpotInfo.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    viewBinding.linearProgressIndicator.visibility = View.GONE
                    updateView(result.value)
                }
                is Result.Loading -> {
                    viewBinding.linearProgressIndicator.visibility = View.VISIBLE
                }
                is Result.Error -> {
                    viewBinding.linearProgressIndicator.visibility = View.GONE
                    ErrorUtil.networkError(result.e)
                }
            }
        }
    }

    private fun updateView(info: ScenicSpotInfo) {
        with(viewBinding) {
            viewImage.load(info.pictures.firstOrNull())
            displayTitleView(textTitle, info.name, info.websiteUrl.isNullOrBlank().not()) {
                intentWeb(info.websiteUrl!!)
            }

            val hasAddressPosition = info.position?.lon != null && info.position?.lat != null
            displayTitleView(
                textAddress,
                info.address ?: if (hasAddressPosition) getString(R.string.map) else "",
                hasAddressPosition
            ) {
                intentMap(info.name, info.position!!.lat!!, info.position!!.lon!!)
            }
            displayTitleView(textTel, info.phone, info.phone.isNullOrBlank().not()) {
                intentPhoneDial(info.phone!!)
            }

            val hasParkingPosition =
                info.parkingPosition?.lat != null && info.parkingPosition?.lon != null
            displayTitleView(
                textParking,
                info.parkingInfo
                    ?: if (hasParkingPosition) getString(R.string.parking_info) else "",
                hasParkingPosition
            ) {
                intentMap("", info.parkingPosition!!.lat!!, info.parkingPosition!!.lon!!)
            }

            displayTextView(
                textOpenTime,
                info.openTime,
                textOpenTimeTitle,
                getString(R.string.open_time)
            )
            displayTextView(textRemark, info.remarks, textRemarkTitle, getString(R.string.remark))
            displayTextView(
                textTravelInfo,
                info.travelInfo,
                textTravelInfoTitle,
                getString(R.string.travel_info)
            )
            displayTextView(
                textDescription,
                info.descriptionDetail ?: info.description,
                textDescriptionTitle,
                getString(R.string.travel_description)
            )

            viewStar.setOnClickListener { view ->
                info.star = !info.star
                view.isActivated = !view.isActivated
                viewModel.clickStar(info)
            }
            viewPushPin.setOnClickListener { view ->
                info.pin = !info.pin
                view.isActivated = !view.isActivated
                viewModel.clickPushPin(info)
            }
            viewStar.isActivated = info.star
            viewPushPin.isActivated = info.pin

            textCity.visibility =
                if (info.city?.value.isNullOrBlank()) {
                    View.GONE
                } else {
                    textCity.text = info.city!!.value
                    View.VISIBLE
                }
            textZipcode.visibility =
                if (info.zipCodeName.isNullOrBlank()) {
                    View.GONE
                } else {
                    textZipcode.text = info.zipCodeName
                    View.VISIBLE
                }
        }
    }

    private fun intentWeb(uri: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
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

    private fun displayTitleView(
        view: TextView,
        text: String?,
        hasActionInfo: Boolean,
        actionEvent: (() -> Unit)? = null
    ) {
        if (text?.isNotBlank() == true) {
            view.text = text
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }

        view.compoundDrawables.also { drawables ->
            view.setCompoundDrawablesWithIntrinsicBounds(
                drawables[0],
                drawables[1],
                if (hasActionInfo) {
                    view.setOnClickListener { actionEvent?.invoke() }
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_baseline_open_in_new_18,
                        null
                    )
                } else {
                    null
                },
                drawables[3]
            )
        }
    }

    private fun displayTextView(
        view: TextView,
        text: String?,
        titleView: TextView? = null,
        titleText: String = ""
    ) {
        if (text?.isNotBlank() == true) {
            view.text = text
            view.visibility = View.VISIBLE
            titleView?.text = titleText
            titleView?.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
            titleView?.visibility = View.GONE
        }
    }
}