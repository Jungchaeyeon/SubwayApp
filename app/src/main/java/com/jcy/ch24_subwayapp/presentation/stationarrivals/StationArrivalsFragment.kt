package com.jcy.ch24_subwayapp.presentation.stationarrivals

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jcy.ch24_subwayapp.R
import com.jcy.ch24_subwayapp.databinding.FragmentStationArrivalsBinding
import com.jcy.ch24_subwayapp.domain.ArrivalInformation
import com.jcy.ch24_subwayapp.extension.toGone
import com.jcy.ch24_subwayapp.extension.toVisible
import org.koin.android.scope.ScopeFragment
import org.koin.core.parameter.parametersOf

class StationArrivalsFragment : ScopeFragment(), StationArrivalsContract.View {

    private var binding: FragmentStationArrivalsBinding? = null
    private val arguments: StationArrivalsFragmentArgs by navArgs()

    override val presenter: StationArrivalsContract.Presenter by inject { parametersOf(arguments.station) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentStationArrivalsBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViews()
        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_station_arrivals, menu)
        menu.findItem(R.id.favoriteAction).apply {
            setIcon(
                if (arguments.station.isFavorited) {
                    R.drawable.ic_baseline_star_24
                } else {
                    R.drawable.ic_baseline_star_border_24
                }
            )
            isChecked = arguments.station.isFavorited
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.refreshAction -> {
                presenter.fetchStationArrivals()
                true
            }
            R.id.favoriteAction -> {
                item.isChecked = !item.isChecked
                item.setIcon(
                    if (item.isChecked) {
                        R.drawable.ic_baseline_star_24
                    } else {
                        R.drawable.ic_baseline_star_border_24
                    }
                )
                presenter.toggleStationFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun showLoadingIndicator() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        binding?.progressBar?.visibility = View.GONE
    }

    override fun showErrorDescription(message: String) {
        binding?.recyclerView?.toGone()
        binding?.errorDescriptionTextView?.toVisible()
        binding?.errorDescriptionTextView?.text = message
    }

    override fun showStationArrivals(arrivalInformation: List<ArrivalInformation>) {
        binding?.errorDescriptionTextView?.toGone()
        (binding?.recyclerView?.adapter as? StationArrivalsAdapter)?.run {
            this.data = arrivalInformation
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = StationArrivalsAdapter()
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun bindViews() {}
}